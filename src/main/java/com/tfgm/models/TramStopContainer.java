package com.tfgm.models;

public class TramStopContainer {
  private TramStop tramStop;
  private TramLinkStop tramLinkStop;

  public TramStopContainer(TramStop tramStop) {
    this.tramStop = tramStop;
    tramLinkStop = new TramLinkStop();
  }

  public TramStop getTramStop() {
    return tramStop;
  }

  public void setTramStop(TramStop tramStop) {
    this.tramStop = tramStop;
  }

  public TramLinkStop getTramLinkStop() {
    return tramLinkStop;
  }

  public void setTramLinkStop(TramLinkStop tramLinkStop) {
    this.tramLinkStop = tramLinkStop;
  }
}
