package com.tfgm.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * The main TramStop entity. This entity represents the combination of a tram stop and direction.
 *
 * @author aelina
 */
public class TramStop {

  /** The official name of the tram stop. */
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
  private final List<String> lastUpdated = new ArrayList<>();

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

  public List<String> getLastUpdated() {
    return lastUpdated;
  }

  public void addToLastUpdated(String lastUpdated) {
    this.lastUpdated.add(lastUpdated);
  }

  public Queue<Tram> getTramQueue() {
    return tramQueue;
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
