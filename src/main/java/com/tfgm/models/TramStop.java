package com.tfgm.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The main TramStop entity. This entity represents the combination of a tram stop and direction.
 *
 * @author aelina
 */
public class TramStop {

  /** The offical name of the tram stop. */
  private final String stopName;

  /**
   * The direction a tram stop is going. Incoming is towards the centre of manchester, Outgoing is
   * away.
   */
  private final String direction;

  /** The overall line the stop is on. */
  private final String line;

  /**
   * An arraylist of the previous updates this TramStop has had. Generally in the format
   * "YYYYMMDDHHMM{DestinationName}."
   */
  private final ArrayList<String> lastUpdated = new ArrayList<>();

  /** A representation of Trams currently at this Station going in a direction. */
  private final Queue<Tram> tramQueue = new LinkedList<>();

  /**
   * The next nodes in the graph. Please see {@link com.tfgm.models.TramStopContainer} for more
   * details.
   */
  private TramStopContainer[] nextStops;
  /**
   * The next previous nodes in the graph. Please see {@link com.tfgm.models.TramStopContainer} for
   * more details.
   */
  private TramStopContainer[] prevStops;

  /**
   * Constructor for class TramStop.
   *
   * @param stopName Name of the stop.
   * @param direction Direction of stop.
   * @param line Line of the stop.
   */
  public TramStop(String stopName, String direction, String line) {
    this.stopName = stopName;
    this.direction = direction;
    this.line = line;
  }

  /**
   * A method to set the previous and next stops. These are in the {@link
   * com.tfgm.models.TramStopContainer} form as they require a "link".
   *
   * @param prevStops The previous stops.
   * @param nextStops The next stops.
   */
  public void setPrevAndNextStops(TramStopContainer[] prevStops, TramStopContainer[] nextStops) {
    this.prevStops = prevStops;
    this.nextStops = nextStops;
  }

  /**
   * An override of the toString method that avoids infinite loops.
   *
   * @return A string version of the class.
   */
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

  /**
   * Simple nextStops getter.
   *
   * @return Returns the nextStops array
   */
  public TramStopContainer[] getNextStops() {
    return nextStops;
  }

  /**
   * Simple prevStops getter.
   *
   * @return Returns the prevStops array
   */
  public TramStopContainer[] getPrevStops() {
    return prevStops;
  }


  public String getStopName() {
    return stopName;
  }

  public String getDirection() {
    return direction;
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
          return;
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

  private boolean findEndOfLine(String endOfLine, TramStop tramStop) {
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
    TramStop other = (TramStop) obj;
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
