package com.tfgm.services;

import com.tfgm.models.Tram;
import com.tfgm.models.TramNetworkDTO;
import com.tfgm.persistence.TramNetworkDTORepo;
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

  @Autowired private TramNetworkDTORepo tramNetworkRepo;

  public TramNetworkService(TramNetworkDTORepo tramNetworkRepo) throws IOException {
    this.tramNetworkRepo = tramNetworkRepo;
  }

  public List<TramNetworkDTO> getAllTrams() {
    List<TramNetworkDTO> tramNetworkDTOList = tramNetworkRepo.getAll();

    return tramNetworkDTOList;
  }

  public TramNetworkDTO getByTimestamp(Long timestamp) {
    return tramNetworkRepo.findByTimestamp(timestamp);
  }

  public List<Long> getAllTimestamps() {
    List<Long> tramNetworkDTOTimestampList = tramNetworkRepo.getAllTimestamps();

    return tramNetworkDTOTimestampList;
  }
//
//  public List<String> getAllTramsAt(String tramStopName) {
//    List<Tram> tramList = getLatestTramInfo();
//
//    String tramStopRegex = tramStopName + "Outgoing|" + tramStopName + "Incoming";
//
//    tramList =
//        tramList.stream()
//            .filter(
//                m ->
//                    m.getDestination().matches(tramStopRegex)
//                        && m.getOrigin().matches(tramStopRegex))
//            .toList();
//
//    return tramList.stream().map(m -> m.toJSONString()).toList();
//  }
//
  public String getAllTramsAtTimestamp(Long timestamp) {
    List<Tram> tramList = getLatestTramInfo(timestamp);

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

  private List<Tram> getLatestTramInfo(Long timestamp) {
    System.out.println("latestInfoDBin" + Instant.now());
    TramNetworkDTO tramNetworkDTO = tramNetworkRepo.findByTimestamp(timestamp);
    System.out.println("latestInfoDBOut:" + Instant.now());


    return new ArrayList<>(tramNetworkDTO.getTramArrayList());
  }
}
