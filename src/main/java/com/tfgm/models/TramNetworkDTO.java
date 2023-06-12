package com.tfgm.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.TimeSeries;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

@TimeSeries(collection="tramnetwork", timeField = "timestamp")
public class TramNetworkDTO {

    @Id
    private String id;
    private Long timestamp;
    private ArrayList<Tram> tramArrayList;


    public TramNetworkDTO() {
    }

    public TramNetworkDTO(String id, Long timestamp, ArrayList<Tram> tramArrayList) {
        this.id = id;
        this.timestamp = timestamp;
        this.tramArrayList = tramArrayList;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
