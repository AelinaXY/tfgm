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
    private Instant timestamp;
    private ArrayList<Tram> tramArrayList;


    public TramNetworkDTO() {
    }

    public TramNetworkDTO(String id, Instant timestamp, ArrayList<Tram> tramArrayList) {
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

}
