package com.tfgm.persistence;

import com.tfgm.models.Tram;
import com.tfgm.models.TramNetworkDTO;
import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import java.time.Instant;
import java.util.*;

import org.springframework.stereotype.Repository;


@Repository
public class TramNetworkDTORepoCustomImpl implements TramNetworkDTORepoCustom {

     private final TramNetworkDTORepoBasic repository;
     private final TramRepo tramRepo;

    public TramNetworkDTORepoCustomImpl(TramNetworkDTORepoBasic tramNetworkDTORepoBasic, TramRepo tramRepo) {
        this.repository = tramNetworkDTORepoBasic;
        this.tramRepo = tramRepo;
    }

    public void saveTramNetwork(Map<String, TramStop> tramStopHashMap) {

    List<Tram> allTrams = new ArrayList<>();

    for (TramStop tramStop : tramStopHashMap.values()) {

      allTrams.addAll(tramStop.getTramQueue());
      TramStopContainer[] nextStops = tramStop.getNextStops();

      for (TramStopContainer nextTramStop : nextStops) {
        allTrams.addAll(nextTramStop.getTramLinkStop().getTramQueue());
      }
    }

    tramRepo.saveAll(allTrams);

    repository.save(new TramNetworkDTO(Instant.now().getEpochSecond(), allTrams));
  }
}
