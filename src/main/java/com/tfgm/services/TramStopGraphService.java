package com.tfgm.services;

import com.tfgm.models.Tram;
import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import java.util.Queue;

public class TramStopGraphService {

  private final TramStopServiceUtilities utilities = new TramStopServiceUtilities();

  public void tramDeparture(String endOfLine, TramStop tramStop) {
    Tram departingTram;
    Queue<Tram> tramQueue = tramStop.getTramQueue();

    if (!tramQueue.isEmpty()) {
      departingTram = tramQueue.remove();
    } else {
      System.out.println("Tram created at " + tramStop.getStopName() + ".");
      departingTram = new Tram(endOfLine);
    }

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
                  + departingTram.getEndOfLine());
          departingTram.setDestination(rawNameToCompositeName(tramStopContainer.getTramStop()));
          departingTram.setOrigin(rawNameToCompositeName(tramStop));
          return;
        }
      }
    }
  }

  public void tramArrival(TramStop tramStop) {

    TramStopContainer[] prevStops = tramStop.getPrevStops();
    Queue<Tram> tramQueue = tramStop.getTramQueue();

    for (TramStopContainer tramStopContainer : prevStops) {
      if (!tramStopContainer.getTramLinkStop().isTramQueueEmpty()) {
        Tram arrivedTram = tramStopContainer.getTramLinkStop().popTram();

        if (!arrivedTram.getEndOfLine().equals(tramStop.getStopName())) {
          tramQueue.add(arrivedTram);
          assert tramQueue.peek() != null;
          System.out.println(
              "Tram arrived at "
                  + tramStop.getStopName()
                  + " from "
                  + tramStopContainer.getTramStop().getStopName()
                  + ". Final Destination: "
                  + tramQueue.peek().getEndOfLine());
          tramQueue.peek().setOrigin(rawNameToCompositeName(tramStop));
        } else {
          System.out.println("Tram arrived at " + tramStop.getStopName() + ". END OF LINE");
        }
        return;
      }
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
    return utilities.cleanStationName(tramStop.getStopName()) + tramStop.getDirection();
  }
}
