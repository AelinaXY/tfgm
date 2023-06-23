package com.tfgm.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TramNetworkDTO {

  private UUID uuid;

  private Long timestamp;

  private List<Tram> tramArrayList = new ArrayList<>();

  public TramNetworkDTO() {}

  public TramNetworkDTO(Long timestamp, List<Tram> tramArrayList) {
    this.timestamp = timestamp;
    this.tramArrayList = tramArrayList;
  }

  public TramNetworkDTO(UUID id, Long timestamp, String tramArrayList) {
    this.uuid = id;
    this.timestamp = timestamp;
    this.tramArrayList = null;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "TramNetworkDTO{" + "timestamp=" + timestamp + ", tramArrayList=" + tramArrayList + '}';
  }

  public String toJSONString() {
    return "{"
        + "\"timestamp\":"
        + timestamp
        + ", \"tramArrayList\":"
        + tramArrayList.stream().map(m -> m.toJSONString()).toList()
        + '}';
  }

  public List<Tram> getTramArrayList() {
    return tramArrayList;
  }
}
