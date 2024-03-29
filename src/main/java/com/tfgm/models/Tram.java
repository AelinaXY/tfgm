package com.tfgm.models;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Tram {

  private UUID uuid;
  private String endOfLine;

  private String destination;

  private String origin;

  private Long lastUpdated;

  private Long population;

  private Map<String, Long> tramHistory = new HashMap<>();

  private TramNetworkDTO tramNetworkDTO;

  private boolean toRemove = false;

  public Tram(String endOfLine) {
    this.endOfLine = endOfLine;
  }

  public Tram(String endOfLine, Long lastUpdated) {
    this.endOfLine = endOfLine;
    this.lastUpdated = lastUpdated;
  }

  public Tram(UUID uuid, String endOfLine, Long lastUpdated) {
    this.uuid = uuid;
    this.endOfLine = endOfLine;
    this.lastUpdated = lastUpdated;
  }

  public Tram(
      UUID uuid,
      String endOfLine,
      String destination,
      String origin,
      Long lastUpdated,
      Map<String, Long> tramHistory) {
    this.uuid = uuid;
    this.endOfLine = endOfLine;
    this.destination = destination;
    this.origin = origin;
    this.lastUpdated = lastUpdated;
    this.tramHistory = tramHistory;
  }

  public Tram(
      UUID uuid,
      String endOfLine,
      String destination,
      String origin,
      Long lastUpdated,
      Long population,
      Map<String, Long> tramHistory) {
    this.uuid = uuid;
    this.endOfLine = endOfLine;
    this.destination = destination;
    this.origin = origin;
    this.lastUpdated = lastUpdated;
    this.population = population;
    this.tramHistory = tramHistory;
  }

  public Tram() {}

  public String getEndOfLine() {
    return endOfLine;
  }

  public String getOrigin() {
    return origin;
  }

  public String getDestination() {
    return destination;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public Long getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Long lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public void addToTramHistory(String stop, Long timestamp) {
    tramHistory.put(stop, timestamp);
  }

  public Map<String, Long> getTramHistory() {
    return new HashMap<>(tramHistory);
  }

  public UUID getUuid() {
    return uuid;
  }

  public Long getPopulation() {
    return population;
  }

  public void setPopulation(Long population) {
    this.population = population;
  }

    public boolean isToRemove() {
        return toRemove;
    }

    public void setToRemove(boolean toRemove) {
        this.toRemove = toRemove;
    }

    @Override
  public String toString() {
    return "Tram{"
        + "uuid="
        + uuid
        + ", endOfLine='"
        + endOfLine
        + '\''
        + ", destination='"
        + destination
        + '\''
        + ", origin='"
        + origin
        + '\''
        + ", lastUpdated="
        + lastUpdated
        + ", population="
        + population
        + ", tramHistory="
        + tramHistory
        + '}';
  }

  public String toJSONString() {
    return "{"
        + "\"uuid\":\""
        + uuid
        + "\""
        + ", \"endOfLine\":\""
        + endOfLine
        + '\"'
        + ", \"destination\":\""
        + destination
        + '\"'
        + ", \"origin\":\""
        + origin
        + '\"'
        + ", \"population\":\""
        + population
        + '\"'
        + '}';
  }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tram tram = (Tram) o;

        if (toRemove != tram.toRemove) return false;
        if (!uuid.equals(tram.uuid)) return false;
        if (!Objects.equals(endOfLine, tram.endOfLine)) return false;
        if (!Objects.equals(destination, tram.destination)) return false;
        return Objects.equals(origin, tram.origin);
    }

    @Override
  public int hashCode() {
    int result = uuid != null ? uuid.hashCode() : 0;
    result = 31 * result + (endOfLine != null ? endOfLine.hashCode() : 0);
    result = 31 * result + (destination != null ? destination.hashCode() : 0);
    result = 31 * result + (origin != null ? origin.hashCode() : 0);
    result = 31 * result + (tramNetworkDTO != null ? tramNetworkDTO.hashCode() : 0);
    return result;
  }
}
