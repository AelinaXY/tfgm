package com.tfgm.models;

import java.util.UUID;

public class JourneyTime {

  UUID uuid;
  String origin;
  String destination;
  Long time = 0l;
  Long averageCount = 0l;

  public JourneyTime(UUID uuid, String origin, String destination) {
    this.uuid = uuid;
    this.destination = destination;
    this.origin = origin;
  }

    public JourneyTime(UUID uuid, String origin, String destination, Long time, Long averageCount) {
        this.uuid = uuid;
        this.origin = origin;
        this.destination = destination;
        this.time = time;
        this.averageCount = averageCount;
    }

    public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public Long getTime() {
    return time;
  }

  public Long getAverageCount() {
    return averageCount;
  }

  public Long updateAverage(Long newTime) {

    Long tempAverage = time * averageCount;
    averageCount++;
    tempAverage += newTime;

    time = tempAverage / averageCount;

    return time;
  }

  @Override
  public String toString() {
    return "\nJourneyTime{"
        + "origin='"
        + origin
        + '\''
        + ", destination='"
        + destination
        + '\''
        + ", time="
        + time
        + ", averageCount="
        + averageCount
        + "}";
  }
}
