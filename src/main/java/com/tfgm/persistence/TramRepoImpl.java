package com.tfgm.persistence;

import com.tfgm.models.Tram;
import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import com.tfgm.persistence.mapper.TramMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class TramRepoImpl implements TramRepo {

  private final TramMapper tramMapper;

  public TramRepoImpl(TramMapper tramMapper) {
    this.tramMapper = tramMapper;
  }

  @Override
  public void saveTrams(Map<String, TramStop> tramStopHashMap) {

    List<Tram> allTrams = new ArrayList<>();

    for (TramStop tramStop : tramStopHashMap.values()) {

      allTrams.addAll(tramStop.getTramQueue());

      for (TramStopContainer nextTramStop : tramStop.getNextStops()) {
        allTrams.addAll(nextTramStop.getTramLinkStop().getTramQueue());
      }
    }

    System.out.println("SAVING TRAMS: " + Instant.now());
    for (Tram tram : allTrams) {
      tramMapper.create(tram);
    }
    System.out.println("DONE SAVING TRAMS: " + Instant.now());
  }

    @Override
    public Tram delete(UUID uuid) {
        return tramMapper.delete(uuid);
    }

    @Override
    public int deleteAll() {
      return tramMapper.deleteAll();

    }

    @Override
    public List<Tram> getInNextTwoHours(Long timestamp, String stopName) {
        return tramMapper.getAfterTS(timestamp,stopName);
    }
}
