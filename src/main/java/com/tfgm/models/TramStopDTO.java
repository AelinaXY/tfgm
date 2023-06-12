package com.tfgm.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Document(collection = "tramstopdata")
public class TramStopDTO {

  @Id private String id;

    private String tramStopName;
    private String direction;
  private String line;
  private String location;
  private String[] nextStop;
  private String[] prevStop;

  public TramStopDTO() {}

  public TramStopDTO(
      String id,
      String direction,
      String line,
      String location,
      String[] nextStop,
      String[] prevStop,
      String tramStopName) {
    this.id = id;
    this.direction = direction;
    this.line = line;
    this.location = location;
    this.nextStop = nextStop;
    this.prevStop = prevStop;
    this.tramStopName = tramStopName;
  }

  public String getId() {
    return id;
  }

    public void setId(String id) {
    this.id = id;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String[] getNextStop() {
    return nextStop;
  }

  public void setNextStop(String[] nextStop) {
    this.nextStop = nextStop;
  }

  public String[] getPrevStop() {
    return prevStop;
  }

  public void setPrevStop(String[] prevStop) {
    this.prevStop = prevStop;
  }

  public String getTramStopName() {
    return tramStopName;
  }

  public void setTramStopName(String tramStopName) {
    this.tramStopName = tramStopName;
  }

    @Override
    public String toString() {
        return "TramStopData{" +
            "id='" + id + '\'' +
            ", direction='" + direction + '\'' +
            ", line='" + line + '\'' +
            ", location='" + location + '\'' +
            ", nextStop=" + Arrays.toString(nextStop) +
            ", prevStop=" + Arrays.toString(prevStop) +
            ", tramStopName='" + tramStopName + '\'' +
            '}';
    }
}
