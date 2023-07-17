package com.tfgm.models;

public class TramUpdate {
    private final String station;
    private final String status;
    private final Long updatePosition;

    private boolean paired = false;

    public TramUpdate(String station, String status, Long updatePosition) {
        this.station = station;
        this.status = status;
        this.updatePosition = updatePosition;
    }

    public Long getUpdatePosition() {
        return updatePosition;
    }

    public String getStation() {
        return station;
    }

    public String getStatus() {
        return status;
    }

    public boolean isPaired() {
        return paired;
    }

    public void setPaired(boolean paired) {
        this.paired = paired;
    }

    @Override
    public String toString() {
        return "TramUpdate{" +
            "station='" + station + '\'' +
            ", status='" + status + '\'' +
            ", updatePosition=" + updatePosition +
            '}';
    }
}
