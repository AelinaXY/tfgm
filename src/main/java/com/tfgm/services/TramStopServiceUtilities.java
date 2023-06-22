package com.tfgm.services;

import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class TramStopServiceUtilities {

    private TramStopServiceUtilities() {
    }

    public static String getUpdateString(JSONObject currentStation, int j) {
    return currentStation.getString("LastUpdated").substring(0, 15)
        + currentStation.getString("Dest" + j)
        + "Dest"
        + j
        + currentStation.getString("Status" + j);
  }

  public static String cleanStationName(String stationName) {
    return stationName.replaceAll("[^A-Za-z]+", "");
  }
}
