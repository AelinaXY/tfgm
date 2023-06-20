package com.tfgm.models;

import java.util.LinkedList;
import java.util.Queue;

public class TramLinkStop {

  private final Queue<Tram> tramQueue = new LinkedList<>();

  public void addTram(Tram tram) {
    tramQueue.add(tram);
  }

  public Tram popTram() {
    return tramQueue.remove();
  }

  public int queueLength() {
    return tramQueue.size();
  }

  public boolean isTramQueueEmpty() { return tramQueue.isEmpty();}

    public Queue<Tram> getTramQueue() {
        return tramQueue;
    }
}
