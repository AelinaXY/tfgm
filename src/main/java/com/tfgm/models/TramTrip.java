package com.tfgm.models;

public class TramTrip {
    private String destination, status;
    private int carriages, wait;


    public TramTrip(String destination, int carriages, String status, int wait) {
        this.destination = destination;
        this.status = status;
        this.carriages = carriages;
        this.wait = wait;
    }


    public String getDestination() {
        return destination;
    }


    public void setDestination(String destination) {
        this.destination = destination;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public int getCarriages() {
        return carriages;
    }


    public void setCarriages(int carriages) {
        this.carriages = carriages;
    }


    public int getWait() {
        return wait;
    }


    public void setWait(int wait) {
        this.wait = wait;
    }


    @Override
    public String toString() {
        return "Tram to " + destination + " is " + status + " with " + carriages + " carriages in "
                + wait + " minute" + (wait != 1 ? "s":"");
    }

    

    
}
