package com.tfgm.models;

import java.util.UUID;

public class TramHistory {
    private UUID tramHistoryId;
    private UUID tramId;
    private String origin;
    private String destination;
    private Long timeAtOrigin;
    private Long timeAtDestination;
    private String status;

    public TramHistory(UUID tramHistoryId, UUID tramId, String origin, String destination, Long timeAtOrigin, Long timeAtDestination, String status) {
        this.tramHistoryId = tramHistoryId;
        this.tramId = tramId;
        this.origin = origin;
        this.destination = destination;
        this.timeAtOrigin = timeAtOrigin;
        this.timeAtDestination = timeAtDestination;
        this.status = status;
    }

    public UUID getTramHistoryId() {
        return tramHistoryId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getTimeAtOrigin() {
        return timeAtOrigin;
    }


    public long getTimeAtDestination() {
        return timeAtDestination;
    }
}
