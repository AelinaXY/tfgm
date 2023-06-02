package com.tfgm.models;

import java.lang.reflect.Array;
import java.util.Arrays;

public class NewTramStop {

    private String stopName, direction, line;

    private NewTramStop[] nextStops,prevStops;

    public NewTramStop(String stopName, String direction, String line) {
        this.stopName = stopName;
        this.direction = direction;
        this.line = line;
    }

    public void setPrevAndNextStops(NewTramStop[] prevStops, NewTramStop[] nextStops)
    {
        this.prevStops = prevStops;
        this.nextStops = nextStops;
    }

    @Override
    public String toString() {
        return "NewTramStop [stopName=" + stopName + ", direction=" + direction + ", line=" + line +
        ", nextStops: " + (nextStops.length > 0 ? Arrays.stream(nextStops).map(n -> n.getStopName()).toList(): "none") +
        ", prevStops: " + (prevStops.length > 0 ? Arrays.stream(prevStops).map(n -> n.getStopName()).toList() : "none") +
        "]";
    }

    public NewTramStop[] getNextStops() {
        return nextStops;
    }

    public NewTramStop[] getPrevStops() {
        return prevStops;
    }

    public String getStopName() {
        return stopName;
    }

    public String getDirection() {
        return direction;
    }

    public String getLine() {
        return line;
    }

    

    

    
}
