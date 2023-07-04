package com.tfgm.models;

import java.util.UUID;

public class Person {

  private UUID uuid;
  private String name;
  private Long tapInTime;
  private String tapInStop;
  private Long tapOutTime;
  private String tapOutStop;

  public Person(
      UUID uuid,
      String name,
      Long tapInTime,
      String tapInStop,
      Long tapOutTime,
      String tapOutStop) {
    this.uuid = uuid;
    this.name = name;
    this.tapInTime = tapInTime;
    this.tapInStop = tapInStop;
    this.tapOutTime = tapOutTime;
    this.tapOutStop = tapOutStop;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getTapInTime() {
    return tapInTime;
  }

  public void setTapInTime(Long tapInTime) {
    this.tapInTime = tapInTime;
  }

  public String getTapInStop() {
    return tapInStop;
  }

  public void setTapInStop(String tapInStop) {
    this.tapInStop = tapInStop;
  }

  public Long getTapOutTime() {
    return tapOutTime;
  }

  public void setTapOutTime(Long tapOutTime) {
    this.tapOutTime = tapOutTime;
  }

  public String getTapOutStop() {
    return tapOutStop;
  }

  public void setTapOutStop(String tapOutStop) {
    this.tapOutStop = tapOutStop;
  }
}
