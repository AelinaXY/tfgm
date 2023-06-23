package com.tfgm.models;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

public class Tram {

  private UUID uuid;
  private String endOfLine;

    private String destination;

    private String origin;


  private TramNetworkDTO tramNetworkDTO;

  public Tram(String endOfLine) {
    this.endOfLine = endOfLine;
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
        + '}';
  }

  public String toJSONString() {
    return "{"
        + "\"uuid\":"
        + uuid
        + ", \"endOfLine\":\""
        + endOfLine
        + '\"'
        + ", \"destination\":\""
        + destination
        + '\"'
        + ", \"origin\":\""
        + origin
        + '\"'
        + '}';
  }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tram tram = (Tram) o;

        if (!Objects.equals(uuid, tram.uuid)) return false;
        if (!Objects.equals(endOfLine, tram.endOfLine)) return false;
        if (!Objects.equals(destination, tram.destination)) return false;
        if (!Objects.equals(origin, tram.origin)) return false;
        return Objects.equals(tramNetworkDTO, tram.tramNetworkDTO);
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
