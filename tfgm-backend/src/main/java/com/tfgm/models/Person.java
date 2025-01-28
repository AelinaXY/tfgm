package com.tfgm.models;

import java.util.Objects;
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

    public Person(UUID uuid) {
        this.uuid = uuid;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Person person = (Person) o;

    if (!Objects.equals(name, person.name)) return false;
    if (!Objects.equals(tapInTime, person.tapInTime)) return false;
    if (!Objects.equals(tapInStop, person.tapInStop)) return false;
    if (!Objects.equals(tapOutTime, person.tapOutTime)) return false;
    return Objects.equals(tapOutStop, person.tapOutStop);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (tapInTime != null ? tapInTime.hashCode() : 0);
    result = 31 * result + (tapInStop != null ? tapInStop.hashCode() : 0);
    result = 31 * result + (tapOutTime != null ? tapOutTime.hashCode() : 0);
    result = 31 * result + (tapOutStop != null ? tapOutStop.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Person{"
        + "uuid="
        + uuid
        + ", name='"
        + name
        + '\''
        + ", tapInTime="
        + tapInTime
        + ", tapInStop='"
        + tapInStop
        + '\''
        + ", tapOutTime="
        + tapOutTime
        + ", tapOutStop='"
        + tapOutStop
        + '\''
        + '}';
  }
}
