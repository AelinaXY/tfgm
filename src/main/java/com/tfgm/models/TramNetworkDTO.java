package com.tfgm.models;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.TimeSeries;

@TimeSeries(collection = "tramnetwork", timeField = "timestamp")
public class TramNetworkDTO {

private Long timestamp;

  private ArrayList<Tram> tramArrayList;

  public TramNetworkDTO() {}

  public TramNetworkDTO(Long timestamp, ArrayList<Tram> tramArrayList) {
    this.timestamp = timestamp;
    this.tramArrayList = tramArrayList;
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

  public ArrayList<Tram> getTramArrayList() {
    return tramArrayList;
  }
}
