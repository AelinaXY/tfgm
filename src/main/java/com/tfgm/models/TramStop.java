package com.tfgm.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class TramStop {

  private final String stopName;

  private final String direction;

  private final String line;

  private final ArrayList<String> lastUpdated = new ArrayList<>();

  private final Queue<Tram> tramQueue = new LinkedList<>();

  private TramStopContainer[] nextStops;

  private TramStopContainer[] prevStops;

  public TramStop(String stopName, String direction, String line) {
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

  public ArrayList<String> getLastUpdated() {
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
