package com.tfgm.persistence;

import com.tfgm.models.TramNetworkDTO;
import com.tfgm.models.TramStop;
import java.util.List;
import java.util.Map;

/** */
public interface TramNetworkDTORepo {
  List<TramNetworkDTO> getAll();

  List<Long> getAllTimestamps();

  void saveTramNetwork(Map<String, TramStop> tramStopHashMap);
}
