package com.tfgm.models;

public class TramJourneyResponse {

    private Tram firstTram;
    private Tram secondTram;

    private Long firstTramArrivalTime;
    private Long secondTramArrivalTime;
    private Long journeyLength;

    public TramJourneyResponse() {
    }

    public Tram getFirstTram() {
        return firstTram;
    }

    public void setFirstTram(Tram firstTram) {
        this.firstTram = firstTram;
    }

    public Tram getSecondTram() {
        return secondTram;
    }

    public void setSecondTram(Tram secondTram) {
        this.secondTram = secondTram;
    }

    public Long getFirstTramArrivalTime() {
        return firstTramArrivalTime;
    }

    public void setFirstTramArrivalTime(Long firstTramArrivalTime) {
        this.firstTramArrivalTime = firstTramArrivalTime;
    }

    public Long getSecondTramArrivalTime() {
        return secondTramArrivalTime;
    }

    public void setSecondTramArrivalTime(Long secondTramArrivalTime) {
        this.secondTramArrivalTime = secondTramArrivalTime;
    }

    public Long getJourneyLength() {
        return journeyLength;
    }

    public void setJourneyLength(Long journeyLength) {
        this.journeyLength = journeyLength;
    }

    @Override
    public String toString() {
        return "TramJourneyResponse{" +
            "firstTram=" + firstTram +
            ", secondTram=" + secondTram +
            ", firstTramArrivalTime=" + firstTramArrivalTime +
            ", secondTramArrivalTime=" + secondTramArrivalTime +
            ", journeyLength=" + journeyLength +
            '}';
    }
}
