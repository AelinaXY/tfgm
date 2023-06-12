package com.tfgm.models;

public class Tram {

  private int id;
  private String endOfLine;
  private String destination;
  private String origin;

  public Tram(int id, String endOfLine) {
    this.id = id;
    this.endOfLine = endOfLine;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEndOfLine() {
    return endOfLine;
  }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
    this.destination = destination;
  }

  @Override
  public String toString() {
    return "Tram [id=" + id + ", destination=" + destination + "]";
  }
}
