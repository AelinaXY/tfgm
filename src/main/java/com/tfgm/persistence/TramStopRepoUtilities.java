package com.tfgm.persistence;

import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONArray;

public class TramStopRepoUtilities {
  public TramStopContainer[] findStopLinks(
      String[] arrayOfStops, HashMap<String, TramStop> tramStopHashMap) {
    // Takes a JSON array of stops and finds them in the hashmap. The found stops are converted to
    // TramStopContainers and added to an array. This is so that queues may be added to the edges of
    // the graph
    return Arrays.stream(arrayOfStops)
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

  public String[] jsonArrayToStringArray(JSONArray jsonArray) {
    return jsonArray.toList().stream().map(Object::toString).toArray(String[]::new);
  }
}
