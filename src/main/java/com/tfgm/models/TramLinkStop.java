package com.tfgm.models;

import java.util.LinkedList;
import java.util.Queue;

public class TramLinkStop {
    
    Queue<Tram> tramQueue = new LinkedList<>();

    public void addTram(Tram tram)
    {
        tramQueue.add(tram);
    }

    public Tram popTram()
    {
        return tramQueue.remove();
    }


}