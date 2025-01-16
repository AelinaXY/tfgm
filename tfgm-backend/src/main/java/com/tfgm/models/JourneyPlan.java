package com.tfgm.models;

public class JourneyPlan {

  private String start = "None";

  private String end = "None";
  private String firstGetOff = "None";
  private String firstGetOn = "None";
  private String secondGetOff = "None";
  private String secondGetOn = "None";
  private String secondChangeLegal = "None";
  private String firstChangeLegal = "None";

  public JourneyPlan() {}

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public String getFirstGetOff() {
    return firstGetOff;
  }

  public void setFirstGetOff(String firstGetOff) {
    this.firstGetOff = firstGetOff;
  }

  public String getFirstGetOn() {
    return firstGetOn;
  }

  public void setFirstGetOn(String firstGetOn) {
    this.firstGetOn = firstGetOn;
  }

  public String getSecondGetOff() {
    return secondGetOff;
  }

  public void setSecondGetOff(String secondGetOff) {
    this.secondGetOff = secondGetOff;
  }

  public String getSecondGetOn() {
    return secondGetOn;
  }

  public void setSecondGetOn(String secondGetOn) {
    this.secondGetOn = secondGetOn;
  }

  public String getSecondChangeLegal() {
    return secondChangeLegal;
  }

  public void setSecondChangeLegal(String secondChangeLegal) {
    this.secondChangeLegal = secondChangeLegal;
  }

  public String getFirstChangeLegal() {
    return firstChangeLegal;
  }

  public void setFirstChangeLegal(String firstChangeLegal) {
    this.firstChangeLegal = firstChangeLegal;
  }
}
