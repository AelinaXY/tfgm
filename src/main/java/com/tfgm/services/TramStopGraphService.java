package com.tfgm.services;

import com.tfgm.models.Tram;
import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import java.time.Instant;
import java.util.Queue;
import java.util.UUID;
import org.json.JSONObject;

public class TramStopGraphService {

  public void tramDeparture(
      String endOfLine, TramStop tramStop, Long timestamp, JSONObject currentStation) {
    Tram departingTram = null;
    Queue<Tram> tramQueue = tramStop.getTramQueue();

    if (!tramQueue.isEmpty()) {
      for (Tram tram : tramQueue) {
        if (tram.getEndOfLine().equals(endOfLine)) {
          departingTram = tram;
          tramQueue.remove(tram);
          break;
        }
      }
    }

    if (departingTram == null) {
      System.out.println("Tram created at " + tramStop.getStopName() + ".");
      departingTram = new Tram(UUID.randomUUID(), endOfLine, timestamp);
      System.out.println("DEPARTTRAM: " + departingTram);
        System.out.println(currentStation);
    }

    departingTram.addToTramHistory(tramStop.getStopName(), timestamp);

    TramStopContainer[] nextStops = tramStop.getNextStops();

    for (TramStopContainer tramStopContainer : nextStops) {

      if (!(("Exchange Square".equals(tramStopContainer.getTramStop().getStopName())))
          || endOfLine.matches("East Didsbury|Shaw and Crompton|Rochdale Town Centre")) {

        // NB: Due to the nature of the recursive algorithim the final destination of Eccles via
        // MediaCityUK and Ashton via MCUK needs to be changed to MediaCityUK so the tramstop can be
        // found.
        if (findEndOfLine(
            departingTram.getEndOfLine().contains("MCUK")
                    || departingTram.getEndOfLine().contains("MediaCityUK")
                ? "MediaCityUK"
                : departingTram.getEndOfLine(),
            tramStopContainer.getTramStop())) {
          tramStopContainer.getTramLinkStop().addTram(departingTram);
          System.out.println(
              "Tram left from "
                  + tramStop.getStopName()
                  + " to "
                  + tramStopContainer.getTramStop().getStopName()
                  + ". Final Destination: "
                  + departingTram.getEndOfLine()
                  + "       UUID:"
                  + departingTram.getUuid());
          departingTram.setDestination(rawNameToCompositeName(tramStopContainer.getTramStop()));
          departingTram.setOrigin(rawNameToCompositeName(tramStop));
          departingTram.setLastUpdated(timestamp);
          return;
        }
      }
    }
    System.out.println("TRAM ISSUE: " + departingTram + " created but not left");
  }

  // LEGACY CODE
  //  public void tramArrival(TramStop tramStop) {
  //
  //    TramStopContainer[] prevStops = tramStop.getPrevStops();
  //    Queue<Tram> tramQueue = tramStop.getTramQueue();
  //
  //    for (TramStopContainer tramStopContainer : prevStops) {
  //      if (!tramStopContainer.getTramLinkStop().isTramQueueEmpty()) {
  //        tramArrivalHelper(tramStop, tramQueue, tramStopContainer);
  //      }
  //    }
  //  }

  public void tramArrival(String endOfLine, TramStop tramStop, JSONObject currentStation) {

    TramStopContainer[] prevStops = tramStop.getPrevStops();
    Queue<Tram> tramQueue = tramStop.getTramQueue();

    for (TramStopContainer tramStopContainer : prevStops) {
      if (!tramStopContainer.getTramLinkStop().isTramQueueEmpty()) {
        for (Tram tram : tramStopContainer.getTramLinkStop().getTramQueue()) {
          if (tram.getEndOfLine().equals(endOfLine)) {
            System.out.println("TramARRIVE: " + tram);
            System.out.println("TFGM API: " + currentStation);

            tramArrivalHelper(tramStop, tramQueue, tramStopContainer);
            return;
          }
        }
      }
    }
  }

  private void tramArrivalHelper(
      TramStop tramStop, Queue<Tram> tramQueue, TramStopContainer tramStopContainer) {
    Tram arrivedTram = tramStopContainer.getTramLinkStop().popTram();

    if (!arrivedTram.getEndOfLine().equals(tramStop.getStopName())) {
      arrivedTram.setOrigin(rawNameToCompositeName(tramStop));
      arrivedTram.setLastUpdated(Instant.now().getEpochSecond());

      tramQueue.add(arrivedTram);
      assert tramQueue.peek() != null;
      System.out.println(
          "Tram arrived at "
              + tramStop.getStopName()
              + " from "
              + tramStopContainer.getTramStop().getStopName()
              + ". Final Destination: "
              + arrivedTram.getEndOfLine()
              + "       UUID:"
              + arrivedTram.getUuid());
    } else {
      System.out.println("Tram arrived at " + tramStop.getStopName() + ". END OF LINE");
    }
  }

  private boolean findEndOfLine(String endOfLine, TramStop tramStop) {
    if (tramStop.getStopName().equals(endOfLine)) {
      return true;
    }

    for (TramStopContainer tramStopContainer : tramStop.getNextStops()) {
      boolean res = findEndOfLine(endOfLine, tramStopContainer.getTramStop());
      if (res) {
        return true;
      }
    }
    return false;
  }

  private String rawNameToCompositeName(TramStop tramStop) {
    return TramStopServiceUtilities.cleanStationName(tramStop.getStopName())
        + tramStop.getDirection();
  }
}
