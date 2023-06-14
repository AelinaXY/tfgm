package com.tfgm.services;

import com.tfgm.models.Tram;
import com.tfgm.models.TramNetworkDTO;
import com.tfgm.persistence.TramNetworkRepo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TramNetworkService {

  @Autowired private TramNetworkRepo tramNetworkRepo;

  public TramNetworkService(TramNetworkRepo tramNetworkRepo) throws IOException {
    this.tramNetworkRepo = tramNetworkRepo;
  }

  public List<String> getAllTrams() {
    List<TramNetworkDTO> tramNetworkDTOList = tramNetworkRepo.getAllTrams();

    return tramNetworkDTOList.stream().map(m -> m.toJSONString()).toList();
  }

  public TramNetworkDTO getByTimestamp(Long timestamp) {
    return tramNetworkRepo.getByTimestamp(timestamp);
  }

  public List<Long> getAllTimestamps() {
    List<TramNetworkDTO> tramNetworkDTOList = tramNetworkRepo.getAllTrams();

    return tramNetworkDTOList.stream().map(m -> m.getTimestamp()).toList();
  }

  public List<String> getAllTramsAt(String tramStopName) {
    List<TramNetworkDTO> tramNetworkDTOList = tramNetworkRepo.getAllTrams();

    TramNetworkDTO latestTramInfo =
        tramNetworkDTOList.stream()
            .reduce(
                (first, second) -> (first.getTimestamp() > second.getTimestamp()) ? first : second)
            .get();

    List<Tram> tramList = new ArrayList<>(latestTramInfo.getTramArrayList());

    String tramStopRegex = tramStopName + "Outgoing|" + tramStopName + "Incoming";

    tramList = tramList.stream().filter(m -> m.getDestination().matches(tramStopRegex) && m.getOrigin().matches(tramStopRegex)).toList();

    return tramList.stream().map(m -> m.toJSONString()).toList();
  }
}
