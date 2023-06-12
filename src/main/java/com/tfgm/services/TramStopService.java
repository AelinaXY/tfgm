package com.tfgm.services;

import com.tfgm.models.TramNetworkDTO;
import com.tfgm.models.TramStop;
import com.tfgm.persistence.TramNetworkRepo;
import com.tfgm.persistence.TramStopRepo;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TramStopService {
  private HashMap<String, TramStop> tramStopHashMap;
  private final TramStopGraphService tramStopGraphService = new TramStopGraphService();

  private final TramStopServiceUtilities utilities = new TramStopServiceUtilities();

  @Autowired
  private TramNetworkRepo tramNetworkRepo;

  @Autowired
  private TramStopRepo tramStopRepo;

  public TramStopService(TramStopRepo tramStopRepo, TramNetworkRepo tramNetworkRepo) throws IOException {
    this.tramStopRepo = tramStopRepo;
    this.tramNetworkRepo = tramNetworkRepo;
  }

  public void update() throws URISyntaxException, IOException {

    tramStopHashMap = tramStopRepo.getTramStops();
    HttpClient httpclient = HttpClients.createDefault();

    URIBuilder builder = new URIBuilder("https://api.tfgm.com/odata/Metrolinks");
    // Example data from TFGM API
    // {"StationLocation":"Withington", "AtcoCode":"9400ZZMAWIT2", "Direction":"Outgoing",
    // "Dest0":"East Didsbury", "Carriages0":"Single", "Status0":"Due", "Wait0":"3",
    // "Dest1":"East Didsbury", "Carriages1":"Double", "Status1":"Due", "Wait1":"14",
    // "Dest2":"East Didsbury", "Carriages2":"Single", "Status2":"Due", "Wait2":"16",
    // "Dest3":"", "Carriages3":"", "Status3":"", "Wait3":"",
    // "MessageBoard":"Welcome to Metrolink. For up to date travel information contact Metrolink
    // twitter on @MCRMetrolink or visit www.TfGM.com. Take care of each other Manchester.",
    // "LastUpdated":"2023-05-26T15:29:19Z" }

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

          if (!nextDestinationNull) {
            String nextStatus = currentStation.getString("Status" + j);
            boolean isTramDeparting = nextStatus.equals("Departing");
            boolean isTramArriving = nextStatus.equals("Arrived");

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

      tramNetworkRepo.dumpTramNetwork(tramStopRepo.getTramStops());
    }
  }
}
