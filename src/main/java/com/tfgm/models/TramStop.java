package com.tfgm.models;

import java.util.ArrayList;

public class TramStop {

  private String stopName, direction, line;
  private int id;
  private ArrayList<TramTrip> tramTrips = new ArrayList<>();

  public TramStop(String stopName, String line, String direction, int id) {
    this.stopName = stopName;
    this.line = line;
    this.direction = direction;
    this.id = id;
  }

  public TramStop(TramStop tramStop) {
    this.stopName = tramStop.getStopName();
    this.line = tramStop.getLine();
    this.direction = tramStop.getDirection();
    this.line = tramStop.getLine();
    this.id = tramStop.getId();
    this.tramTrips = tramStop.getTramTrips();
  }

  public boolean addTramTrip(TramTrip tramTrip) {
    return tramTrips.add(tramTrip);
  }

  public String getStopName() {
    return stopName;
  }

  public void setStopName(String stopName) {
    this.stopName = stopName;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public ArrayList<TramTrip> getTramTrips() {
    return tramTrips;
  }

  public void setTramTrips(ArrayList<TramTrip> tramTrips) {
    this.tramTrips = tramTrips;
  }

  @Override
  public String toString() {
    return "\nOn the line "
        + line
        + ",stop "
        + stopName
        + " in the direction "
        + direction
        + " has trams: "
        + tramTrips;
  }
}
