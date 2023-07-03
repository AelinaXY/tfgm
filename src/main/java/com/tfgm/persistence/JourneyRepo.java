package com.tfgm.persistence;

import com.tfgm.models.Journey;
import com.tfgm.models.Tram;
import com.tfgm.models.TramStop;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** */
public interface JourneyRepo {
  void saveJourneys(List<Journey> journeyList);
  }
