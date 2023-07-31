package com.tfgm.persistence;

import com.tfgm.models.Tram;
import com.tfgm.models.TramNetworkDTO;
import com.tfgm.models.TramStop;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** */
public interface TramRepo {
  void saveTrams(Map<String, TramStop> tramStopHashMap);
  Tram delete(UUID uuid);

  int deleteAll();

  List<Tram> getInNextTwoHours(Long timestamp, String stopName);

  List<Tram> getAll();
}
