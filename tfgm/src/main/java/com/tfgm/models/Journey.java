package com.tfgm.models;

import java.util.Objects;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Journey journey = (Journey) o;

    if (!Objects.equals(getOnTime, journey.getOnTime)) return false;
    if (!Objects.equals(getOnStop, journey.getOnStop)) return false;
    if (!Objects.equals(getOffTime, journey.getOffTime)) return false;
    if (!Objects.equals(getOffStop, journey.getOffStop)) return false;
    if (!Objects.equals(tramUUID, journey.tramUUID)) return false;
    return Objects.equals(personUUID, journey.personUUID);
  }

  @Override
  public int hashCode() {
    int result = getOnTime != null ? getOnTime.hashCode() : 0;
    result = 31 * result + (getOnStop != null ? getOnStop.hashCode() : 0);
    result = 31 * result + (getOffTime != null ? getOffTime.hashCode() : 0);
    result = 31 * result + (getOffStop != null ? getOffStop.hashCode() : 0);
    result = 31 * result + (tramUUID != null ? tramUUID.hashCode() : 0);
    result = 31 * result + (personUUID != null ? personUUID.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Journey{"
        + "uuid="
        + uuid
        + ", getOnTime="
        + getOnTime
        + ", getOnStop='"
        + getOnStop
        + '\''
        + ", getOffTime="
        + getOffTime
        + ", getOffStop='"
        + getOffStop
        + '\''
        + ", tramUUID="
        + tramUUID
        + ", personUUID="
        + personUUID
        + '}';
  }
}
