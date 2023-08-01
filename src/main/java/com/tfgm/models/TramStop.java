package com.tfgm.models;

import com.tfgm.services.TramStopService;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main TramStop entity. This entity represents the combination of a tram stop and direction.
 *
 * @author aelina
 */
public class TramStop {

  /** The official name of the tram stop. */
  private final String stopName;

  /**
   * The direction a tram stop is going. Incoming is towards the centre of manchester, Outgoing is
   * away.
   */
  private final String direction;

  /** The overall line the stop is on. */
  private final List<String> line;

  /** An arraylist of the previous updates this TramStop has had." */
  private final List<TramUpdate> lastUpdated = new ArrayList<>();

  /** A representation of Trams currently at this Station going in a direction. */
  private final Queue<Tram> tramQueue = new LinkedList<>();

  /**
   * The next nodes in the graph. Please see {@link com.tfgm.models.TramStopContainer} for more
   * details.
   */
  private TramStopContainer[] nextStops;
  /**
   * The next previous nodes in the graph. Please see {@link com.tfgm.models.TramStopContainer} for
   * more details.
   */
  private TramStopContainer[] prevStops;

  private int lastUpdateCount = 0;

  private static Logger logger = LoggerFactory.getLogger("analytics");

  /**
   * Constructor for class TramStop.
   *
   * @param stopName Name of the stop.
   * @param direction Direction of stop.
   * @param line Line of the stop.
   */
  public TramStop(String stopName, String direction, String[] line) {

      if (!stopName.trim().equals("")) {
      this.stopName = stopName;
    } else {
      throw new IllegalArgumentException("stopName is '" + stopName + "'");
    }
    if (!direction.trim().equals("")) {
      this.direction = direction;
    } else {
      throw new IllegalArgumentException("direction is '" + direction + "'");
    }
    if (!(line.length == 0)) {
      this.line = List.of(line);
    } else {
      throw new IllegalArgumentException("line is '" + line + "'");
    }
  }

  /**
   * A method to set the previous and next stops. These are in the {@link
   * com.tfgm.models.TramStopContainer} form as they require a "link".
   *
   * @param prevStops The previous stops.
   * @param nextStops The next stops.
   */
  public void setPrevAndNextStops(TramStopContainer[] prevStops, TramStopContainer[] nextStops) {
    this.prevStops = prevStops;
    this.nextStops = nextStops;
  }

  /**
   * An override of the toString method that avoids infinite loops.
   *
   * @return A string version of the class.
   */
  @Override
  public String toString() {
    return "TramStop{"
        + "stopName='"
        + stopName
        + '\''
        + ", direction='"
        + direction
        + '\''
        + ", line="
        + line
        + ", lastUpdated="
        + lastUpdated
        + ", tramQueue="
        + tramQueue
        + ", nextStops: "
        + (Arrays.stream(nextStops).toList())
        + ", prevStops: "
        + (Arrays.stream(prevStops).toList())
        + ", lastUpdateCount="
        + lastUpdateCount
        + '}';
  }

  /**
   * Simple nextStops getter.
   *
   * @return Returns the nextStops array
   */
  public TramStopContainer[] getNextStops() {
    return nextStops;
  }

  /**
   * Simple prevStops getter.
   *
   * @return Returns the prevStops array
   */
  public TramStopContainer[] getPrevStops() {
    return prevStops;
  }

  public String getStopName() {
    return stopName;
  }

  public String getDirection() {
    return direction;
  }

  public int getLastUpdatedSize() {
    return lastUpdated.size();
  }

  public String getLastUpdatedString() {
    return lastUpdated.toString();
  }

  public void addToLastUpdated(String station, String status, Long position) {
    this.lastUpdated.add(new TramUpdate(station, status, position));
  }

  public boolean isValidTram(String station, String status, Long position) {
    List<TramUpdate> correctUpdateCode =
        lastUpdated.stream().filter(m -> m.getStation().equals(station)).toList();

    Long arrivalCount =
        correctUpdateCode.stream().filter(m -> m.getStatus().equals("Arrived")).count();
    Long departureCount =
        correctUpdateCode.stream().filter(m -> m.getStatus().equals("Departing")).count();

    if (departureCount > 0 && arrivalCount > departureCount && status.equals("Departing")) {
      logger.warn("DEPARTMULTITRAM");
      return true;
    }

    correctUpdateCode =
        correctUpdateCode.stream().filter(m -> m.getStatus().equals(status)).toList();

    for (int i = 0; i < correctUpdateCode.size(); i++) {
      if (position <= correctUpdateCode.get(i).getUpdatePosition()) {
        return false;
      }
    }
    return true;
  }

  public void clearLastUdated() {
    lastUpdated.clear();
  }

  public void clearLastUpdated() {
    List<TramUpdate> toClearList;
    for (TramUpdate tramUpdate : lastUpdated) {
      if (!tramUpdate.isPaired()) {
        String departOrArrive = tramUpdate.getStatus().equals("Arrived") ? "Departing" : "Arrived";

        TramUpdate pairedTramUpdate =
            lastUpdated.stream()
                .filter(
                    m ->
                        !m.isPaired()
                            && m.getStation().equals(tramUpdate.getStation())
                            && m.getStatus().equals(departOrArrive))
                .findFirst()
                .orElse(null);

        if (pairedTramUpdate != null) {
          pairedTramUpdate.setPaired(true);
          tramUpdate.setPaired(true);
        }
      }
    }

    for (TramUpdate tramUpdate : lastUpdated) {
      if (!tramUpdate.isPaired() && tramUpdate.getStatus().equals("Arrived")) {
        for (Tram tram : tramQueue) {
          if (tram.getEndOfLine().equals(tramUpdate.getStation())) {
            logger.debug("REMOVED DUE TO ORPHANAGE: " + this + "\n | " + tram);
            tram.setToRemove(true);
            break;
          }
        }
      }
    }

    lastUpdated.clear();
  }

  public Queue<Tram> getTramQueue() {
    return tramQueue;
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TramStop tramStop = (TramStop) o;

    if (!stopName.equals(tramStop.stopName)) return false;
    return direction.equals(tramStop.direction);
  }

  public int getLastUpdateCount() {
    return lastUpdateCount;
  }

  public void incrementLastUpdateCount() {
    lastUpdateCount++;
  }

  public void zeroLastUpdateCount() {
    lastUpdateCount = 0;
  }
}
