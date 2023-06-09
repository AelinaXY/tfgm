package com.tfgm.controllers;

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

public class TramController {

  private final HashMap<String, TramStop> tramStopHashMap = new HashMap<>();

  public TramController(String tramDataPath) throws IOException {
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
      JSONObject obj = new JSONObject(jsonString);
      JSONArray array = new JSONArray(obj.getJSONArray("value"));

      for (int i = 0; i < array.length(); i++) {
        JSONObject currentStation = array.getJSONObject(i);

        for (int j = 0; j < 4; j++) {
          if (!(currentStation.getString("Dest" + j).equals(""))) {
            if (currentStation.getString("Status" + j).equals("Departing")) {
              TramStop tramStop =
                  tramStopHashMap.get(
                      currentStation.getString("StationLocation").replaceAll("[^A-Za-z]+", "")
                          + currentStation.getString("Direction"));

              if (tramStop != null) {
                if (!tramStop.getLastUpdated().contains((getUpdateString(currentStation, j)))) {
                  tramStop.addToLastUpdated(getUpdateString(currentStation, j));

                  /*if (currentStation.getString("StationLocation").equals("Exchange Square")
                      && currentStation.getString("Dest" + j).equals("East Didsbury")) {
                    System.out.println(currentStation);
                  }*/

                  tramStop.tramDeparture(currentStation.getString("Dest" + j));
                }
              }
            }

            if (currentStation.getString("Status" + j).equals("Arrived")) {
              TramStop tramStop =
                  tramStopHashMap.get(
                      currentStation.getString("StationLocation").replaceAll("[^A-Za-z]+", "")
                          + currentStation.getString("Direction"));
              if (tramStop != null) {

                if (!tramStop.getLastUpdated().contains((getUpdateString(currentStation, j)))) {
                  tramStop.addToLastUpdated(getUpdateString(currentStation, j));
                  tramStop.tramArrival();
                }
              }
            }
          }
        }
      }
    }
  }

  private String getUpdateString(JSONObject currentStation, int j) {
    return currentStation.getString("LastUpdated").substring(0, 15)
        + currentStation.getString("Dest" + j)
        + "Dest"
        + j
        + currentStation.getString("Status" + j);
  }
}
