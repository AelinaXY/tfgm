package com.tfgm.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.UUID;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "tramnetwork")
public class TramNetworkDTO {

  @Id private UUID uuid = UUID.randomUUID();

  @NotNull
  @Column(name = "timestamp")
  private Long timestamp;

  @OneToMany
  @JoinColumn(name = "tramnetwork_uuid")
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
