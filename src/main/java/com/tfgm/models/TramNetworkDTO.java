package com.tfgm.models;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "tramnetwork")
public class TramNetworkDTO {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  @NotNull
  @Column(name = "timestamp")
  private Long timestamp;

  @Column(columnDefinition = "jsonb")
  @Type(JsonType.class)
  private List<Tram> tramArrayList = new ArrayList<>();

  public TramNetworkDTO() {}

  public TramNetworkDTO(Long timestamp, List<Tram> tramArrayList) {
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

  public List<Tram> getTramArrayList() {
    return tramArrayList;
  }
}
