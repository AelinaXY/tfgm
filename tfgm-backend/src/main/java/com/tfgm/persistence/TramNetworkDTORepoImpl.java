package com.tfgm.persistence;

import com.tfgm.models.Tram;
import com.tfgm.models.TramNetworkDTO;
import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import com.tfgm.persistence.mapper.TramNetworkDTOMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class TramNetworkDTORepoImpl implements TramNetworkDTORepo {

  private final TramNetworkDTOMapper TNMapper;

  public TramNetworkDTORepoImpl(TramNetworkDTOMapper TNMapper) {
    this.TNMapper = TNMapper;
  }

  @Override
  public List<TramNetworkDTO> getAll() {
    return TNMapper.getAll();
  }

  @Override
  public List<Long> getAllTimestamps() {
    return TNMapper.getAllTimestamps();
  }

  @Override
  public void saveTramNetwork(Map<String, TramStop> tramStopHashMap, Long timestamp) {

    List<Tram> allTrams = new ArrayList<>();

    for (TramStop tramStop : tramStopHashMap.values()) {

      allTrams.addAll(tramStop.getTramQueue());
      TramStopContainer[] nextStops = tramStop.getNextStops();

      for (TramStopContainer nextTramStop : nextStops) {
        allTrams.addAll(nextTramStop.getTramLinkStop().getTramQueue());
      }
    }

    UUID uuid = UUID.randomUUID();

    TNMapper.create(new TramNetworkDTO(uuid, timestamp, allTrams));
  }

    @Override
    public void saveTramNetworkDTO(TramNetworkDTO tramNetworkDTO) {
      TNMapper.create(tramNetworkDTO);
    }

    @Override
  public TramNetworkDTO findByTimestamp(Long timestamp) {
    return TNMapper.getByTimestamp(timestamp);
  }


}
