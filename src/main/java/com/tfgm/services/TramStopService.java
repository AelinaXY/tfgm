package com.tfgm.services;

import com.tfgm.models.TFGMResponse;
import com.tfgm.models.Tram;
import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import com.tfgm.persistence.TFGMResponseRepo;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TramStopService {
  private Map<String, TramStop> tramStopHashMap;

  private final Long timeToLive = 480L;

  private final Long timeToDest = 300L;
  private final TramStopGraphService tramStopGraphService = new TramStopGraphService();

  @Autowired private TramNetworkDTORepo tramNetworkDTORepo;

  @Autowired private TramRepo tramRepo;

  @Autowired private TramStopRepo tramStopRepo;
  @Autowired private TFGMResponseRepo tfgmResponseRepo;

  private static Logger logger = LoggerFactory.getLogger("network");
  private static Logger loggerMove = LoggerFactory.getLogger("moving");
  private static Logger loggerDelete = LoggerFactory.getLogger("analytics");

  public TramStopService(
      TramStopRepo tramStopRepo,
      TramNetworkDTORepo tramNetworkDTORepo,
      TramRepo tramRepo,
      TFGMResponseRepo tfgmResponseRepo)
      throws IOException {
    this.tramStopRepo = tramStopRepo;
    this.tramNetworkDTORepo = tramNetworkDTORepo;
    this.tramRepo = tramRepo;
    this.tfgmResponseRepo = tfgmResponseRepo;
  }

  public void updateRecorded() throws IOException {
    tramStopHashMap = tramStopRepo.getTramStops();
    System.out.println("Acquiring TFGM Response data");
    List<TFGMResponse> tfgmResponseList = tfgmResponseRepo.getAllResponse();

    for (TFGMResponse response : tfgmResponseList) {
      runDataInstance(response.getResponse(), response.getTimestamp());
    }
  }

  public void updateLive() throws URISyntaxException, IOException {

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
      runDataInstance(tfgmFullResponse, timestamp);
      savetfgmResponse(tfgmFullResponse, timestamp);
    }
  }

  private void savetfgmResponse(JSONObject tfgmFullResponse, Long timestamp) {
    tfgmResponseRepo.saveResponse(new TFGMResponse(timestamp, tfgmFullResponse));
  }

  private void runDataInstance(JSONObject tfgmFullResponse, Long timestamp) {

    JSONArray tfgmValueArray = new JSONArray(tfgmFullResponse.getJSONArray("value"));

    for (int i = 0; i < tfgmValueArray.length(); i++) {
      JSONObject currentStation = tfgmValueArray.getJSONObject(i);

      for (int j = 0; j < 4; j++) {
        String endOfLine = currentStation.getString("Dest" + j);
        boolean nextDestinationNull = endOfLine.equals("");

        // Sometimes Deansgate - Castlefield ends up without it's dash causing issues
        if (endOfLine.equals("Deansgate Castlefield")) {
          endOfLine = "Deansgate - Castlefield";
        }

        if (endOfLine.equals("Ashton-under-Lyne")) {
          endOfLine = "Ashton-Under-Lyne";
        }

        if (!nextDestinationNull) {
          String nextStatus = currentStation.getString("Status" + j);
          boolean isTramDeparting = nextStatus.equals("Departing");
          boolean isTramArriving = nextStatus.equals("Arrived");

          if (isTramDeparting || isTramArriving) {

            String rawStationName = currentStation.getString("StationLocation");
            String cleanStationName = TramStopServiceUtilities.cleanStationName(rawStationName);
            String stationDirection = currentStation.getString("Direction");

            // For some god forsaken reason, Barton Dock Road is the wrong way around in the API.
            if (rawStationName.equals("Barton Dock Road")) {
              stationDirection = stationDirection.equals("Incoming") ? "Outgoing" : "Incoming";
            }

            String compositeStationName = cleanStationName + stationDirection;

            TramStop foundTramStop = tramStopHashMap.get(compositeStationName);

            if (foundTramStop != null) {

              foundTramStop.incrementLastUpdateCount();

              String stopUpdateCode = TramStopServiceUtilities.getUpdateString(currentStation, j);
              boolean uniqueTramDepartArrive =
                  foundTramStop.isValidTram(endOfLine, nextStatus, (long) j);

              if (uniqueTramDepartArrive
                  && !endOfLine.equals("See Tram Front")
                  && !endOfLine.equals("Not in Service")) {

                foundTramStop.addToLastUpdated(endOfLine, nextStatus, (long) j);

                if (isTramDeparting && !endOfLine.equals("Terminates Here")) {
                  tramStopGraphService.tramDeparture(
                      endOfLine, foundTramStop, timestamp, currentStation);
                } else {
                  tramStopGraphService.tramArrival(
                      endOfLine, foundTramStop, currentStation, timestamp);
                }
              }
            }
          }
        }
      }

      // LOG HELP
      if (currentStation.getString("StationLocation").equals("Newton Heath and Moston")
          && currentStation.getString("Direction").equals("Incoming")) {
        logger.warn("Newton Heath and Moston" + currentStation.getString("Direction") + timestamp);
        logger.warn(currentStation.toString());
        logger.warn(
            tramStopHashMap.get("NewtonHeathandMoston" + currentStation.getString("Direction")).toString());
      }

      if (currentStation.getString("StationLocation").equals("Newton Heath and Moston")
          && currentStation.getString("Direction").equals("Outgoing")) {
        logger.warn("Newton Heath and Moston"+ currentStation.getString("Direction") + timestamp);
        logger.warn(currentStation.toString());
        logger.warn(
            tramStopHashMap.get("NewtonHeathandMoston" + currentStation.getString("Direction")).toString());
      }
    }

    zeroAllStops(tramStopHashMap);
    removeOldTrams(tramStopHashMap, timestamp);
    tramNetworkDTORepo.saveTramNetwork(tramStopHashMap, timestamp);
    tramRepo.saveTrams(tramStopHashMap);
    removeFlaggedTrams(tramStopHashMap);
  }

  private void removeFlaggedTrams(Map<String, TramStop> tramStopHashMap) {

    for (TramStop tramStop : tramStopHashMap.values()) {
      Queue<Tram> tramQueue = tramStop.getTramQueue();

      removeFlaggedTramsLoop(tramQueue);

      if (tramStop.getNextStops() != null) {
        for (TramStopContainer tramStopContainer : tramStop.getNextStops()) {
          Queue<Tram> containerTramQueue = tramStopContainer.getTramLinkStop().getTramQueue();
          removeFlaggedTramsLoop(containerTramQueue);
        }
      }
    }
  }

  private void removeFlaggedTramsLoop(Queue<Tram> tramQueue) {
    List<Tram> listToRemove = new ArrayList<>();

    for (Tram tram : tramQueue) {
      if (tram.isToRemove()) {
        System.out.println("Removed " + tram);
        listToRemove.add(tram);
      }
    }
    if (listToRemove.size() > 0) {
      loggerDelete.warn("RemovingLoop: " + listToRemove.toString());
      tramQueue.removeAll(listToRemove);
    }
  }

  private void removeOldTrams(Map<String, TramStop> tramStopHashMap, Long timestamp) {

    for (TramStop tramStop : tramStopHashMap.values()) {
      Queue<Tram> tramQueue = tramStop.getTramQueue();

      removeOldTramsLoop(timestamp, tramQueue);

      if (tramStop.getNextStops() != null) {
        for (TramStopContainer tramStopContainer : tramStop.getNextStops()) {
          Queue<Tram> containerTramQueue = tramStopContainer.getTramLinkStop().getTramQueue();
          TramStop nextStop = tramStopContainer.getTramStop();

          moveOldTramsLoop(timestamp, containerTramQueue, nextStop);
          removeOldTramsLoop(timestamp, containerTramQueue);
        }
      }
    }
  }

  private void moveOldTramsLoop(Long timestamp, Queue<Tram> tramQueue, TramStop nextStop) {
    List<Tram> listToMove = new ArrayList<>();

    for (Tram tram : tramQueue) {
      if (tram.getLastUpdated() + timeToDest < timestamp
          && TramStopGraphService.getCorrectEndOfLine(tram).equals(nextStop.getStopName())) {
        listToMove.add(tram);
      }
    }

    for (Tram tram : listToMove) {
      loggerMove.debug("Moved " + tram + " to EOL");
      tramStopGraphService.tramArrival(tram.getEndOfLine(), nextStop, new JSONObject(), timestamp);
    }
  }

  private void removeOldTramsLoop(Long timeStamp, Queue<Tram> tramQueue) {
    for (Tram tram : tramQueue) {
      if (tram.getLastUpdated() + timeToLive < timeStamp) {
        System.out.println("Removed " + tram);
        System.out.println("TIME: " + timeStamp);
        System.out.println("LAST UPDATE: " + tram.getLastUpdated());
        tram.setToRemove(true);
        //        tramRepo.delete(tram.getUuid());
      }
    }
  }

  private void zeroAllStops(Map<String, TramStop> tramStopHashMap) {
    for (TramStop tramStop : tramStopHashMap.values()) {

      if (tramStop.getLastUpdatedSize() > 0 && tramStop.getLastUpdateCount() == 0) {
        System.out.println(
            tramStop.getStopName()
                + " | "
                + tramStop.getDirection()
                + " | "
                + tramStop.getLastUpdatedString());
        tramStop.clearLastUpdated();
      }
      tramStop.zeroLastUpdateCount();
    }
  }
}
