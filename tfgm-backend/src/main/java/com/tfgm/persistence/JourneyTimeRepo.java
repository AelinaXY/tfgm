package com.tfgm.persistence;

import com.tfgm.models.JourneyTime;

import java.util.List;

/** */
public interface JourneyTimeRepo {
  void saveJourneyTimes(List<JourneyTime> journeyTimeListList);
  List<JourneyTime> getAll();

  List<JourneyTime> getDestination(String destination);

  JourneyTime getOriginAndDestination(String origin, String destination);
  }
