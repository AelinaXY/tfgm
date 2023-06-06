package com.tfgm.models;

public class TramStopContainer {
  private NewTramStop tramStop;
  private TramLinkStop tramLinkStop;

  public TramStopContainer(NewTramStop tramStop) {
    this.tramStop = tramStop;
    tramLinkStop = new TramLinkStop();
  }

  public NewTramStop getTramStop() {
    return tramStop;
  }

  public void setTramStop(NewTramStop tramStop) {
    this.tramStop = tramStop;
  }

  public TramLinkStop getTramLinkStop() {
    return tramLinkStop;
  }

  public void setTramLinkStop(TramLinkStop tramLinkStop) {
    this.tramLinkStop = tramLinkStop;
  }
}
