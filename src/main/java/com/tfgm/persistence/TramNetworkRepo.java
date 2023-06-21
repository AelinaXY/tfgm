package com.tfgm.persistence;

import com.tfgm.models.Tram;
import com.tfgm.models.TramNetworkDTO;
import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import java.time.Instant;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TramNetworkRepo {

  @Autowired private TramNetworkDTORepo repository;

  @Autowired
  public TramNetworkRepo(TramNetworkDTORepo repository) {
    this.repository = repository;
  }

  public void dumpTramNetwork(Map<String, TramStop> tramStopHashMap) {

    ArrayList<Tram> allTrams = new ArrayList<>();

    for (TramStop tramStop : tramStopHashMap.values()) {

      allTrams.addAll(tramStop.getTramQueue());
      TramStopContainer[] nextStops = tramStop.getNextStops();

      for (TramStopContainer nextTramStop : nextStops) {
        allTrams.addAll(nextTramStop.getTramLinkStop().getTramQueue());
      }
    }

    repository.save(new TramNetworkDTO(Instant.now().getEpochSecond(), allTrams));
  }

  public List<TramNetworkDTO> getAllTrams()
  {
      return repository.findAll();
  }

  public TramNetworkDTO getByTimestamp(Long timestamp)
  {
      return repository.findByTimestamp(timestamp);
  }
}