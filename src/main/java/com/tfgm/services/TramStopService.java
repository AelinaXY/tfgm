package com.tfgm.services;

import com.tfgm.models.Tram;
import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import com.tfgm.persistence.TramNetworkDTORepo;
import com.tfgm.persistence.TramRepo;
import com.tfgm.persistence.TramStopRepo;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
  private Map<String, TramStop> tramStopHashMap;

  private Long timeToLive = 1800L;
  private final TramStopGraphService tramStopGraphService = new TramStopGraphService();

  @Autowired private TramNetworkDTORepo tramNetworkDTORepo;

  @Autowired private TramRepo tramRepo;

  @Autowired private TramStopRepo tramStopRepo;

  public TramStopService(
      TramStopRepo tramStopRepo, TramNetworkDTORepo tramNetworkDTORepo, TramRepo tramRepo)
      throws IOException {
    this.tramStopRepo = tramStopRepo;
    this.tramNetworkDTORepo = tramNetworkDTORepo;
    this.tramRepo = tramRepo;
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

    Long timestamp = Instant.now().getEpochSecond();

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
              String cleanStationName = TramStopServiceUtilities.cleanStationName(rawStationName);
              String stationDirection = currentStation.getString("Direction");
              String compositeStationName = cleanStationName + stationDirection;

              TramStop foundTramStop = tramStopHashMap.get(compositeStationName);

              if (foundTramStop != null) {
                String stopUpdateString =
                    TramStopServiceUtilities.getUpdateString(currentStation, j);
                boolean uniqueTramDeparting =
                    foundTramStop.getLastUpdated().contains(stopUpdateString);

                if (!uniqueTramDeparting) {

                  foundTramStop.addToLastUpdated(stopUpdateString);

                  if (isTramDeparting) {
                    tramStopGraphService.tramDeparture(nextDestination, foundTramStop, timestamp);
                  } else {
                    tramStopGraphService.tramArrival(nextDestination, foundTramStop);
                  }
                }
              }
            }
          }
        }
      }

      removeOldTrams(tramStopHashMap);
      tramNetworkDTORepo.saveTramNetwork(tramStopHashMap, timestamp);
      tramRepo.saveTrams(tramStopHashMap);
    }
  }

  private void removeOldTrams(Map<String, TramStop> tramStopHashMap) {
    Long timeStamp = Instant.now().getEpochSecond();

    for (TramStop tramStop : tramStopHashMap.values()) {
      Queue<Tram> tramQueue = tramStop.getTramQueue();

      removeOldTramsLoop(timeStamp, tramQueue);

      if (tramStop.getNextStops() != null) {
        for (TramStopContainer tramStopContainer : tramStop.getNextStops()) {
          Queue<Tram> containerTramQueue = tramStopContainer.getTramLinkStop().getTramQueue();

          removeOldTramsLoop(timeStamp, containerTramQueue);
        }
      }
    }
  }

  private void removeOldTramsLoop(Long timeStamp, Queue<Tram> tramQueue) {
    List<Tram> listToRemove = new ArrayList<>();
    for (Tram tram : tramQueue) {
      if (tram.getLastUpdated() + timeToLive < timeStamp) {
        System.out.println("Removed " + tram);
        System.out.println("TIME: " + timeStamp);
        System.out.println("LAST UPDATE: " + tram.getLastUpdated());
        listToRemove.add(tram);
        //        tramRepo.delete(tram.getUuid());
      }
    }

    for (Tram tram : listToRemove) {
      tramQueue.remove(tram);
    }
  }
}
