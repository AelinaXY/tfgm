package com.tfgm.models;

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

  /** An arraylist of the previous updates this TramStop has had." */
  private final List<TramUpdate> lastUpdated = new ArrayList<>();

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

  private int lastUpdateCount = 0;

  /**
   * Constructor for class TramStop.
   *
   * @param stopName Name of the stop.
   * @param direction Direction of stop.
   * @param line Line of the stop.
   */
  public TramStop(String stopName, String direction, String line) {

    if (!stopName.trim().equals("")) {
      this.stopName = stopName;
    } else {
      throw new IllegalArgumentException("stopName is '" + stopName + "'");
    }
    if (!direction.trim().equals("")) {
      this.direction = direction;
    } else {
      throw new IllegalArgumentException("direction is '" + direction + "'");
    }
    if (!line.trim().equals("")) {
      this.line = line;
    } else {
      throw new IllegalArgumentException("line is '" + line + "'");
    }
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

  public int getLastUpdatedSize() {
    return lastUpdated.size();
  }

  public String getLastUpdatedString() {
    return lastUpdated.toString();
  }

  public void addToLastUpdated(String updateCode, Long position) {
    this.lastUpdated.add(new TramUpdate(updateCode, position));
  }

  public boolean isValidTram(String updateCode, Long position) {
    List<TramUpdate> correctUpdateCode =
        lastUpdated.stream().filter(m -> m.getUpdateCode().equals(updateCode)).toList();

    for (int i = 0; i < correctUpdateCode.size(); i++) {
      if (position <= correctUpdateCode.get(i).getUpdatePosition()) {
        return false;
      }
    }
    return true;
  }

  public void clearLastUpdated() {
    lastUpdated.clear();
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TramStop tramStop = (TramStop) o;

    if (!stopName.equals(tramStop.stopName)) return false;
    return direction.equals(tramStop.direction);
  }

  public int getLastUpdateCount() {
    return lastUpdateCount;
  }

  public void incrementLastUpdateCount() {
    lastUpdateCount++;
  }

  public void zeroLastUpdateCount() {
    lastUpdateCount = 0;
  }
}
