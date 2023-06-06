package com.tfgm.models;

public class Tram {

  private int id;
  private String destination;

  public Tram(int id, String destination) {
    this.id = id;
    this.destination = destination;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  @Override
  public String toString() {
    return "Tram [id=" + id + ", destination=" + destination + "]";
  }
}
