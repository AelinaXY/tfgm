package com.tfgm.services;

import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class TramStopService {
  private final HashMap<String, TramStop> tramStopHashMap = new HashMap<>();
  private final TramStopGraphService tramStopGraphService = new TramStopGraphService();

  private final TramStopServiceUtilities utilities = new TramStopServiceUtilities();

  public TramStopService(String tramDataPath) throws IOException {
    JSONArray allTramStopData =
        new JSONArray(
            Files.readAllLines(Paths.get(tramDataPath)).stream()
                .map(String::valueOf)
                .collect(Collectors.joining()));

    // System.out.println(allTramStopData);

    for (int i = 0; i < allTramStopData.length(); i++) {
      JSONObject tempStop = allTramStopData.getJSONObject(i);
      tramStopHashMap.put(
          tempStop.getString("tramStopName"),
          new TramStop(
              tempStop.getString("location"),
              tempStop.getString("direction"),
              tempStop.getString("line")));
    }
    for (int i = 0; i < allTramStopData.length(); i++) {
      JSONObject tempStop = allTramStopData.getJSONObject(i);

      TramStop currentTramStop = tramStopHashMap.get(tempStop.getString("tramStopName"));

      currentTramStop.setPrevAndNextStops(
          tempStop.getJSONArray("prevStop").toList().stream()
              .map(tramStopHashMap::get)
              .map(TramStopContainer::new)
              .toArray(TramStopContainer[]::new),
          tempStop.getJSONArray("nextStop").toList().stream()
              .map(tramStopHashMap::get)
              .map(TramStopContainer::new)
              .toArray(TramStopContainer[]::new));
    }

    for (TramStop tramStop : tramStopHashMap.values()) {

      System.out.println(tramStop.toString());
      for (TramStopContainer n : tramStop.getNextStops()) {
        TramStopContainer nextStopPrevReference =
            Arrays.stream(n.getTramStop().getPrevStops())
                .filter(s -> s.getTramStop().equals(tramStop))
                .toList()
                .get(0);

        nextStopPrevReference.setTramLinkStop(n.getTramLinkStop());
      }
    }

    // MAKE SURE YOU REMOVE APOSTROPHES AND WHITESPACE WHEN FINDING TRAMSTOP
    for (TramStop i : tramStopHashMap.values()) {
      System.out.println("\n\n" + i.getStopName() + i.getDirection());
      System.out.println(i);

      System.out.println("\nNextStop:");
      for (TramStopContainer n : i.getNextStops()) {
        System.out.println(n.getTramLinkStop());
      }
      System.out.println("\nPrevStop:");
      for (TramStopContainer n : i.getPrevStops()) {
        System.out.println(n.getTramLinkStop());
      }
    }
  }

  public void update() throws URISyntaxException, IOException {
    HttpClient httpclient = HttpClients.createDefault();

    URIBuilder builder = new URIBuilder("https://api.tfgm.com/odata/Metrolinks");
    // "StationLocation":"Withington","AtcoCode":"9400ZZMAWIT2","Direction":"Outgoing","Dest0":"East
    // Didsbury","Carriages0":"Single","Status0":"Due","Wait0":"3","Dest1":"East
    // Didsbury","Carriages1":"Double","Status1":"Due","Wait1":"14","Dest2":"East
    // Didsbury","Carriages2":"Single","Status2":"Due","Wait2":"16","Dest3":"","Carriages3":"","Status3":"","MessageBoard":"Welcome to Metrolink. For up to date travel information contact Metrolink twitter on @MCRMetrolink or visit www.TfGM.com. Take care of each other Manchester.","Wait3":"","LastUpdated":"2023-05-26T15:29:19Z"

    URI uri = builder.build();
    HttpGet request = new HttpGet(uri);
    request.setHeader("Ocp-Apim-Subscription-Key", "810dfa20bbd44815b07d8cb6f9d3be96");

    HttpResponse response = httpclient.execute(request);
    HttpEntity entity = response.getEntity();

    if (entity != null) {
      String jsonString = EntityUtils.toString(entity);
      JSONObject tfgmFullResponse = new JSONObject(jsonString);
      JSONArray tfgmValueArray = new JSONArray(tfgmFullResponse.getJSONArray("value"));

      for (int i = 0; i < tfgmValueArray.length(); i++) {
        JSONObject currentStation = tfgmValueArray.getJSONObject(i);

        for (int j = 0; j < 4; j++) {
          String nextDestination = currentStation.getString("Dest" + j);
          boolean nextDestinationNull = nextDestination.equals("");

          if (!(nextDestinationNull)) {
            String nextStatus = currentStation.getString("Status" + j);
            boolean isTramDeparting = currentStation.equals("Departing");
            boolean isTramArriving = currentStation.equals("Arrived");

            if (isTramDeparting || isTramArriving) {
              String rawStationName = currentStation.getString("StationLocation");
              String cleanStationName = utilities.cleanStationName(rawStationName);
              String stationDirection = currentStation.getString("Direction");
              String compositeStationName = cleanStationName + stationDirection;

              TramStop foundTramStop = tramStopHashMap.get(compositeStationName);

              if (foundTramStop != null) {
                String stopUpdateString = utilities.getUpdateString(currentStation, j);
                boolean uniqueTramDeparting =
                    foundTramStop.getLastUpdated().contains(stopUpdateString);

                if (!uniqueTramDeparting) {

                  foundTramStop.addToLastUpdated(stopUpdateString);

                  if (isTramDeparting) {
                    tramStopGraphService.tramDeparture(nextDestination, foundTramStop);
                  } else {
                    tramStopGraphService.tramArrival(foundTramStop);
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
