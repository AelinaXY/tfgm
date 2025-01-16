package com.tfgm.models;

public class TramJourneyResponse {

    private Tram firstTram;
    private Tram secondTram;

    private Tram thirdTram;


    private Long firstTramArrivalTime;
    private Long secondTramArrivalTime;

    private Long thirdTramArrivalTime;
    private Long journeyLength;

    private String firstChangeStop;

    private String secondChangeStop;


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

    public Long getThirdTramArrivalTime() {
        return thirdTramArrivalTime;
    }

    public void setThirdTramArrivalTime(Long thirdTramArrivalTime) {
        this.thirdTramArrivalTime = thirdTramArrivalTime;
    }

    public String getFirstChangeStop() {
        return firstChangeStop;
    }

    public void setFirstChangeStop(String firstChangeStop) {
        this.firstChangeStop = firstChangeStop;
    }

    public String getSecondChangeStop() {
        return secondChangeStop;
    }

    public void setSecondChangeStop(String secondChangeStop) {
        this.secondChangeStop = secondChangeStop;
    }

    public Tram getThirdTram() {
        return thirdTram;
    }

    public void setThirdTram(Tram thirdTram) {
        this.thirdTram = thirdTram;
    }

    @Override
    public String toString() {
        return "TramJourneyResponse{" +
            "firstTram=" + firstTram +
            ", secondTram=" + secondTram +
            ", thirdTram=" + thirdTram +
            ", firstTramArrivalTime=" + firstTramArrivalTime +
            ", secondTramArrivalTime=" + secondTramArrivalTime +
            ", thirdTramArrivalTime=" + thirdTramArrivalTime +
            ", journeyLength=" + journeyLength +
            ", firstChangeStop='" + firstChangeStop + '\'' +
            ", secondChangeStop='" + secondChangeStop + '\'' +
            '}';
    }
}
