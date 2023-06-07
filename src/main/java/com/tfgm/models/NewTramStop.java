package com.tfgm.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class NewTramStop {

  private final String stopName;
  private final String direction;
  private final String line;
  //    private String lastUpdated = "Never";
  private final ArrayList<String> lastUpdated = new ArrayList<>();

  private final Queue<Tram> tramQueue = new LinkedList<>();

  private TramStopContainer[] nextStops, prevStops;

  public NewTramStop(String stopName, String direction, String line) {
    this.stopName = stopName;
    this.direction = direction;
    this.line = line;
  }

  public void setPrevAndNextStops(TramStopContainer[] prevStops, TramStopContainer[] nextStops) {
    this.prevStops = prevStops;
    this.nextStops = nextStops;
  }

  @Override
  public String toString() {
    return "NewTramStop [stopName="
        + stopName
        + ", direction="
        + direction
        + ", line="
        + line
        + ", nextStops: "
        + (nextStops.length > 0
            ? Arrays.stream(nextStops).map(n -> n.getTramStop().getStopName()).toList()
            : "none")
        + ", prevStops: "
        + (prevStops.length > 0
            ? Arrays.stream(prevStops).map(n -> n.getTramStop().getStopName()).toList()
            : "none")
        + "]";
  }

  public TramStopContainer[] getNextStops() {
    return nextStops;
  }

  public TramStopContainer[] getPrevStops() {
    return prevStops;
  }

  public String getStopName() {
    return stopName;
  }

  public String getDirection() {
    return direction;
  }

  public String getLine() {
    return line;
  }

  public ArrayList<String> getLastUpdated() {
    return lastUpdated;
  }

  public void addToLastUpdated(String lastUpdated) {
    this.lastUpdated.add(lastUpdated);
  }

  public void tramDeparture(String endOfLine) {
    Tram departingTram;
    if (tramQueue.size() > 0) {
      departingTram = tramQueue.remove();
    } else {
      departingTram = new Tram(endOfLine.length() * stopName.length(), endOfLine);
    }

    for (TramStopContainer tramStopContainer : nextStops) {

      if (!(tramStopContainer.getTramStop().getStopName().equals("Exchange Square"))
          || endOfLine.matches("East Didsbury|Shaw and Crompton|Rochdale Town Centre")) {

        // NB: Due to the nature of the recursive algorithim the final destination of Eccles via
        // MediaCityUK and Ashton via MCUK needs to be changed to MediaCityUK so the tramstop can be
        // found.
        if (findEndOfLine(
            Arrays.stream(departingTram.getDestination().split(" "))
                    .reduce((first, second) -> second)
                    .get()
                    .matches("MCUK|MediaCityUK")
                ? "MediaCityUK"
                : departingTram.getDestination(),
            tramStopContainer.getTramStop())) {
          tramStopContainer.getTramLinkStop().addTram(departingTram);
          System.out.println(
              "Tram left from "
                  + stopName
                  + " to "
                  + tramStopContainer.getTramStop().getStopName()
                  + ". Final Destination: "
                  + departingTram.getDestination());
        }
      }
    }
  }

  public void tramArrival() {
    for (TramStopContainer tramStopContainer : prevStops) {
      if (tramStopContainer.getTramLinkStop().queueLength() > 0) {
        tramQueue.add(tramStopContainer.getTramLinkStop().popTram());
        assert tramQueue.peek() != null;
        System.out.println(
            "Tram arrived at "
                + stopName
                + " from "
                + tramStopContainer.getTramStop().getStopName()
                + ". Final Destination: "
                + tramQueue.peek().getDestination());
        return;
      }
    }
  }

  private boolean findEndOfLine(String endOfLine, NewTramStop tramStop) {
    //    if (tramStop == null) return false;
    if (tramStop.getStopName().equals(endOfLine)) {
      return true;
    }

    for (TramStopContainer tramStopContainer : tramStop.nextStops) {
      boolean res = findEndOfLine(endOfLine, tramStopContainer.getTramStop());
      if (res) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((stopName == null) ? 0 : stopName.hashCode());
    result = prime * result + ((direction == null) ? 0 : direction.hashCode());
    result = prime * result + ((line == null) ? 0 : line.hashCode());
    result = prime * result + Arrays.hashCode(nextStops);
    result = prime * result + Arrays.hashCode(prevStops);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    NewTramStop other = (NewTramStop) obj;
    if (stopName == null) {
      if (other.stopName != null) return false;
    } else if (!stopName.equals(other.stopName)) return false;
    if (direction == null) {
      if (other.direction != null) return false;
    } else if (!direction.equals(other.direction)) return false;
    if (line == null) {
      if (other.line != null) return false;
    } else if (!line.equals(other.line)) return false;
    if (!Arrays.equals(nextStops, other.nextStops)) return false;
    return Arrays.equals(prevStops, other.prevStops);
  }
}
