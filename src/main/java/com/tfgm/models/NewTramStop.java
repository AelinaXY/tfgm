package com.tfgm.models;

import java.util.Arrays;

public class NewTramStop {

    private String stopName, direction, line;

    private TramStopContainer[] nextStops,prevStops;

    public NewTramStop(String stopName, String direction, String line) {
        this.stopName = stopName;
        this.direction = direction;
        this.line = line;
    }

    public void setPrevAndNextStops(TramStopContainer[] prevStops, TramStopContainer[] nextStops)
    {
        this.prevStops = prevStops;
        this.nextStops = nextStops;
    }

    @Override
    public String toString() {
        return "NewTramStop [stopName=" + stopName + ", direction=" + direction + ", line=" + line +
        ", nextStops: " + (nextStops.length > 0 ? Arrays.stream(nextStops).map(n -> n.getTramStop().getStopName()).toList(): "none") +
        ", prevStops: " + (prevStops.length > 0 ? Arrays.stream(prevStops).map(n -> n.getTramStop().getStopName()).toList() : "none") +
        "]";
    }

    public TramStopContainer[] getNextStops() {
        return nextStops;
    }

    public TramStopContainer[] getPrevStops() {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((stopName == null) ? 0 : stopName.hashCode());
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        result = prime * result + ((line == null) ? 0 : line.hashCode());
        result = prime * result + Arrays.hashCode(nextStops);
        result = prime * result + Arrays.hashCode(prevStops);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NewTramStop other = (NewTramStop) obj;
        if (stopName == null) {
            if (other.stopName != null)
                return false;
        } else if (!stopName.equals(other.stopName))
            return false;
        if (direction == null) {
            if (other.direction != null)
                return false;
        } else if (!direction.equals(other.direction))
            return false;
        if (line == null) {
            if (other.line != null)
                return false;
        } else if (!line.equals(other.line))
            return false;
        if (!Arrays.equals(nextStops, other.nextStops))
            return false;
        if (!Arrays.equals(prevStops, other.prevStops))
            return false;
        return true;
    }

    

    

    
}
