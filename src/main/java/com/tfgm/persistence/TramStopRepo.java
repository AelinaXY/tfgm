package com.tfgm.persistence;

import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class TramStopRepo {

//  @Value("${tram.data.url}")
  private String tramDataPath = "src/main/resources/static/TramStopData.json";

  private HashMap<String, TramStop> tramStopHashMap = new HashMap<>();

  private TramStopRepoUtilities utilities = new TramStopRepoUtilities();

  @Autowired
  public TramStopRepo() throws IOException {
    // Reads all of the TramStopData from the static JSON file.
    JSONArray allTramStopData =
        new JSONArray(
            Files.readAllLines(Paths.get(tramDataPath)).stream()
                .map(String::valueOf)
                .collect(Collectors.joining()));

    // Iterates through and adds all the tram stops to a hashmap
    for (int i = 0; i < allTramStopData.length(); i++) {
      JSONObject currentTramStop = allTramStopData.getJSONObject(i);
      String tramStopName = currentTramStop.getString("tramStopName");

      tramStopHashMap.put(
          tramStopName,
          new TramStop(
              // Gets Tram Stop name as shown in official material
              currentTramStop.getString("location"),
              // Gets direction
              currentTramStop.getString("direction"),
              // Gets line
              currentTramStop.getString("line")));
    }

    // Iterates through the tram stop array again to create connections between stations. This is so
    // that we can be sure that all the stops exist before making connections
    for (int i = 0; i < allTramStopData.length(); i++) {

      JSONObject currentTramJson = allTramStopData.getJSONObject(i);
      String tramStopName = currentTramJson.getString("tramStopName");

      TramStop currentTramStop = tramStopHashMap.get(tramStopName);

      JSONArray previousStopJsonArray = currentTramJson.getJSONArray("prevStop");
      JSONArray nextStopJsonArray = currentTramJson.getJSONArray("nextStop");

      TramStopContainer[] previousStops =
          utilities.findStopLinks(previousStopJsonArray, tramStopHashMap);
      TramStopContainer[] nextStops = utilities.findStopLinks(nextStopJsonArray, tramStopHashMap);

      currentTramStop.setPrevAndNextStops(previousStops, nextStops);
    }

    // Iterates through the hashmap and ensures that there is a consistent link stop between two
    // nodes in the graph
    for (TramStop tramStop : tramStopHashMap.values()) {

      System.out.println(tramStop.toString());

      for (TramStopContainer n : tramStop.getNextStops()) {
        TramStopContainer nextStopsContainerToThisStop =
            utilities.findTramStopContainerToTramStop(n, tramStop);

        // Makes sure that this tram stops link stop to the next tram stop is equal to the next tram
        // stops link stop to this tram stop
        nextStopsContainerToThisStop.setTramLinkStop(n.getTramLinkStop());
      }
    }
  }

  public HashMap<String, TramStop> getTramStops() throws IOException {
      return tramStopHashMap;
  }
}
