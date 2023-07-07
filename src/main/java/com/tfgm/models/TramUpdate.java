package com.tfgm.models;

public class TramUpdate {
    private final String updateCode;

    private final Long updatePosition;

    public TramUpdate(String updateCode, Long updatePosition) {
        this.updateCode = updateCode;
        this.updatePosition = updatePosition;
    }

    public String getUpdateCode() {
        return updateCode;
    }

    public Long getUpdatePosition() {
        return updatePosition;
    }


    @Override
    public String toString() {
        return "TramUpdate{" +
            "updateCode='" + updateCode + '\'' +
            ", updatePosition=" + updatePosition +
            '}';
    }
}
