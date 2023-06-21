package com.tfgm.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "trams")
public class Tram {

  @Id private UUID uuid = UUID.randomUUID();
  private String endOfLine;
  private String destination;
  private String origin;

  @ManyToOne
  @JoinColumn(name = "tramnetwork_uuid", insertable = false, updatable = false)
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
}
