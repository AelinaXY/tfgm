package com.tfgm.services;

import com.tfgm.models.Tram;
import com.tfgm.models.TramNetworkDTO;
import com.tfgm.persistence.TramNetworkRepo;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONObject;
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
    List<Tram> tramList = getLatestTramInfo();

    String tramStopRegex = tramStopName + "Outgoing|" + tramStopName + "Incoming";

    tramList =
        tramList.stream()
            .filter(
                m ->
                    m.getDestination().matches(tramStopRegex)
                        && m.getOrigin().matches(tramStopRegex))
            .toList();

    return tramList.stream().map(m -> m.toJSONString()).toList();
  }

  public String getAllTramsAtAllStops() {
      List<Tram> tramList = getLatestTramInfo();

    // Finds all Trams at Stops
    tramList = tramList.stream().filter(m -> m.getDestination().equals(m.getOrigin())).toList();

    JSONObject returnJSONObject = new JSONObject();

    Set<String> uniqueTramStopNameSet = new HashSet<>();

    for (Tram tram : tramList) {
      uniqueTramStopNameSet.add(tram.getDestination());
    }

    for (String tramStopName : uniqueTramStopNameSet) {
      returnJSONObject.put(
          tramStopName,
          tramList.stream()
              .filter(m -> m.getDestination().equals(tramStopName))
              .map(Tram::toJSONString)
              .toList());
    }

    return returnJSONObject.toString();
  }

  private List<Tram> getLatestTramInfo() {
    System.out.println("latestInfoDBin"+Instant.now());
    List<TramNetworkDTO> tramNetworkDTOList = tramNetworkRepo.getAllTrams();
    System.out.println("latestInfoDBOut:"+Instant.now());


      TramNetworkDTO latestTramInfo =
        tramNetworkDTOList.stream()
            .reduce(
                (first, second) -> (first.getTimestamp() > second.getTimestamp()) ? first : second)
            .get();
      System.out.println("latestInfoStreamOut:"+Instant.now());


      return new ArrayList<>(latestTramInfo.getTramArrayList());
  }
}
