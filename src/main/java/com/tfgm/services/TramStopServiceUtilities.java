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
}
