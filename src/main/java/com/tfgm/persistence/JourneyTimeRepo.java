package com.tfgm.persistence;

import com.tfgm.models.JourneyTime;
import com.tfgm.models.Person;
import java.util.List;
import java.util.UUID;

/** */
public interface JourneyTimeRepo {
  void saveJourneyTimes(List<JourneyTime> journeyTimeListList);
  List<JourneyTime> getAll();

  List<JourneyTime> getDestination(String destination);
  }
