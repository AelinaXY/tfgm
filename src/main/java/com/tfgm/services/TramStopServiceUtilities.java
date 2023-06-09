package com.tfgm.services;

import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class TramStopServiceUtilities {

  public String getUpdateString(JSONObject currentStation, int j) {
    return currentStation.getString("LastUpdated").substring(0, 15)
        + currentStation.getString("Dest" + j)
        + "Dest"
        + j
        + currentStation.getString("Status" + j);
  }

  public String cleanStationName(String stationName) {
    return stationName.replaceAll("[^A-Za-z]+", "");
  }

  public TramStopContainer[] findStopLinks(
      JSONArray arrayOfStops, HashMap<String, TramStop> tramStopHashMap) {
    // Takes a JSON array of stops and finds them in the hashmap. The found stops are converted to
    // TramStopContainers and added to an array. This is so that queues may be added to the edges of
    // the graph
    return arrayOfStops.toList().stream()
        .map(tramStopHashMap::get)
        .map(TramStopContainer::new)
        .toArray(TramStopContainer[]::new);
  }

  public TramStopContainer findTramStopContainerToTramStop(
      TramStopContainer nextStopLink, TramStop originalTramStop) {
    // Finds the container that references the passed in tram stop
    return Arrays.stream(nextStopLink.getTramStop().getPrevStops())
        .filter(s -> s.getTramStop().equals(originalTramStop))
        .toList()
        .get(0);
  }
}
