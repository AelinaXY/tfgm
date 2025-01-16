package com.tfgm.services;

import com.tfgm.models.Tram;
import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import java.time.Instant;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import org.json.JSONObject;

public class TramStopGraphService {

  public void tramDeparture(
      String endOfLine, TramStop tramStop, Long timestamp, JSONObject currentStation) {
    Tram departingTram;
    Queue<Tram> tramQueue = tramStop.getTramQueue();

    departingTram = getDepartingTram(endOfLine, null, tramQueue);

    if (departingTram == null) {
      tramArrival(endOfLine, tramStop, currentStation, timestamp);
      departingTram = getDepartingTram(endOfLine, null, tramQueue);
      if (departingTram == null) {

        System.out.println("Tram created at " + tramStop.getStopName() + ".");
        departingTram = new Tram(UUID.randomUUID(), endOfLine, timestamp);
        System.out.println("DEPARTTRAM: " + departingTram);
        System.out.println(currentStation);
      }
    }

    departingTram.addToTramHistory(tramStop.getStopName(), timestamp);

    List<TramStopContainer> nextStops = List.of(tramStop.getNextStops());

    if (nextStops.stream().anyMatch(m -> m.getTramStop().getStopName().equals("Exchange Square"))
        && endOfLine.matches("East Didsbury|Shaw and Crompton|Rochdale Town Centre")) {
      nextStops =
          nextStops.stream()
              .filter(m -> m.getTramStop().getStopName().equals("Exchange Square"))
              .toList();
    } else {
      nextStops =
          nextStops.stream()
              .filter(m -> !m.getTramStop().getStopName().equals("Exchange Square"))
              .toList();
    }

    for (TramStopContainer tramStopContainer : nextStops) {

      // Checks if Exchange Square is in the list of next stops and if the end of line is correct
      // for a tram to be routed through exchange square.
      // If so it changes the next stop to be Exchange Square.
      // This should only ever run once even though it is contained within a loop.

      // NB: Due to the nature of the recursive algorithim the final destination of Eccles via
      // MediaCityUK and Ashton via MCUK needs to be changed to MediaCityUK so the tramstop can be
      // found.
      if (findEndOfLine(getCorrectEndOfLine(departingTram), tramStopContainer.getTramStop())) {
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
    System.out.println("TRAM ISSUE: " + departingTram + " created but not left");
  }

  public static String getCorrectEndOfLine(Tram departingTram) {
    return departingTram.getEndOfLine().contains("MCUK")
            || departingTram.getEndOfLine().contains("MediaCityUK")
        ? "MediaCityUK"
        : departingTram.getEndOfLine();
  }

  private static Tram getDepartingTram(
      String endOfLine, Tram departingTram, Queue<Tram> tramQueue) {
    if (!tramQueue.isEmpty()) {
      for (Tram tram : tramQueue) {
        if (tram.getEndOfLine().equals(endOfLine)) {
          departingTram = tram;
          tramQueue.remove(tram);
          break;
        }
      }
    }
    return departingTram;
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

  public void tramArrival(String endOfLine, TramStop tramStop, JSONObject currentStation, Long timestamp) {

    TramStopContainer[] prevStops = tramStop.getPrevStops();
    Queue<Tram> tramQueue = tramStop.getTramQueue();

    for (TramStopContainer tramStopContainer : prevStops) {
      if (!tramStopContainer.getTramLinkStop().isTramQueueEmpty()) {
        for (Tram tram : tramStopContainer.getTramLinkStop().getTramQueue()) {
          if (tram.getEndOfLine().equals(endOfLine)) {
            System.out.println("TramARRIVE: " + tram);
            System.out.println("TFGM API: " + currentStation);

            Tram arrivedTram = tramStopContainer.getTramLinkStop().popTram(tram);

            tramArrivalHelper(tramStop, tramQueue, tramStopContainer, arrivedTram, timestamp);
            return;
          }
        }
      }
    }
  }

  private void tramArrivalHelper(
      TramStop tramStop,
      Queue<Tram> tramQueue,
      TramStopContainer tramStopContainer,
      Tram arrivedTram,
      Long timestamp) {

    arrivedTram.setOrigin(rawNameToCompositeName(tramStop));
    arrivedTram.setLastUpdated(timestamp);

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

    if (getCorrectEndOfLine(arrivedTram).equals(tramStop.getStopName())) {
      System.out.println("END OF LINE | Setting toRemove True");

      arrivedTram.addToTramHistory(tramStop.getStopName(), timestamp);

      arrivedTram.setToRemove(true);
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
