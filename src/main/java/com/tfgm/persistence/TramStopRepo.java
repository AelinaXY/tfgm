package com.tfgm.persistence;

import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import com.tfgm.models.TramStopDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TramStopRepo {

  private final Map<String, TramStop> tramStopHashMap = new HashMap<>();

  private final TramStopRepoUtilities utilities = new TramStopRepoUtilities();

    private String tramDataPath = "src/main/resources/static/TramStopData.json";

  private TramStopDataRepository repository;

  @Autowired
  public TramStopRepo(TramStopDataRepository tramStopDataRepository) throws IOException {
    // Reads all of the TramStopData from the static JSON file.
    this.repository = tramStopDataRepository;

//        JSONArray allTramStopData =
//            new JSONArray(
//                Files.readAllLines(Paths.get(tramDataPath)).stream()
//                    .map(String::valueOf)
//                    .collect(Collectors.joining()));
//
//        firstRun(allTramStopData);

    List<TramStopDTO> allTramStopDTO = repository.findAll();

    // Iterates through and adds all the tram stops to a hashmap
    for (TramStopDTO currentTramStop : allTramStopDTO) {
      String tramStopName = currentTramStop.getTramStopName();

      tramStopHashMap.put(
          tramStopName,
          new TramStop(
              // Gets Tram Stop name as shown in official material
              currentTramStop.getLocation(),
              // Gets direction
              currentTramStop.getDirection(),
              // Gets line
              currentTramStop.getLine()));
    }

    // Iterates through the tram stop array again to create connections between stations. This is so
    // that we can be sure that all the stops exist before making connections
    for (TramStopDTO currentTramDTO : allTramStopDTO) {

      String tramStopName = currentTramDTO.getTramStopName();

      TramStop currentTramStop = tramStopHashMap.get(tramStopName);

      String[] previousStopArray = currentTramDTO.getPrevStop();
      String[] nextStopArray = currentTramDTO.getNextStop();

      TramStopContainer[] previousStops =
          utilities.findStopLinks(previousStopArray, tramStopHashMap);
      TramStopContainer[] nextStops = utilities.findStopLinks(nextStopArray, tramStopHashMap);

      currentTramStop.setPrevAndNextStops(previousStops, nextStops);
    }

    // Iterates through the hashmap and ensures that there is a consistent link stop between two
    // nodes in the graph
    for (TramStop tramStop : tramStopHashMap.values()) {

      for (TramStopContainer n : tramStop.getNextStops()) {
        TramStopContainer nextStopsContainerToThisStop =
            utilities.findTramStopContainerToTramStop(n, tramStop);

        // Makes sure that this tram stops link stop to the next tram stop is equal to the next tram
        // stops link stop to this tram stop
        nextStopsContainerToThisStop.setTramLinkStop(n.getTramLinkStop());
      }
      System.out.println(tramStop.toString());
    }
  }

  public Map<String, TramStop> getTramStops() throws IOException {
    return tramStopHashMap;
  }

  public void firstRun(JSONArray allTramStopData) {
    for (int i = 0; i < allTramStopData.length(); i++) {
      JSONObject currentStop = allTramStopData.getJSONObject(i);

      String[] nextStops = utilities.jsonArrayToStringArray(currentStop.getJSONArray("nextStop"));
      String[] prevStops = utilities.jsonArrayToStringArray(currentStop.getJSONArray("prevStop"));
      String[] line = utilities.jsonArrayToStringArray(currentStop.getJSONArray("line"));

      repository.save(
          new TramStopDTO(
              currentStop.getString("tramStopName"),
              currentStop.getString("direction"),
              line,
              currentStop.getString("location"),
              nextStops,
              prevStops,
              currentStop.getString("tramStopName")));

      System.out.println("Saved " + i);
    }
  }
}
