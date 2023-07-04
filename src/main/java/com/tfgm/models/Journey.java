package com.tfgm.models;

import java.util.UUID;

public class Journey {

  private UUID uuid;

  private Long getOnTime;
  private String getOnStop;
  private Long getOffTime;
  private String getOffStop;

  private UUID tramUUID;

  private UUID personUUID;

  public Journey(
      UUID uuid,
      Long getOnTime,
      String getOnStop,
      Long getOffTime,
      String getOffStop,
      UUID tramUUID,
      UUID personUUID) {
    this.uuid = uuid;
    this.getOnTime = getOnTime;
    this.getOnStop = getOnStop;
    this.getOffTime = getOffTime;
    this.getOffStop = getOffStop;
    this.tramUUID = tramUUID;
    this.personUUID = personUUID;
  }

  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public Long getGetOnTime() {
    return getOnTime;
  }

  public void setGetOnTime(Long getOnTime) {
    this.getOnTime = getOnTime;
  }

  public String getGetOnStop() {
    return getOnStop;
  }

  public void setGetOnStop(String getOnStop) {
    this.getOnStop = getOnStop;
  }

  public Long getGetOffTime() {
    return getOffTime;
  }

  public void setGetOffTime(Long getOffTime) {
    this.getOffTime = getOffTime;
  }

  public String getGetOffStop() {
    return getOffStop;
  }

  public void setGetOffStop(String getOffStop) {
    this.getOffStop = getOffStop;
  }

  public UUID getTramUUID() {
    return tramUUID;
  }

  public void setTramUUID(UUID tramUUID) {
    this.tramUUID = tramUUID;
  }

  public UUID getPersonUUID() {
    return personUUID;
  }

  public void setPersonUUID(UUID personUUID) {
    this.personUUID = personUUID;
  }
}
