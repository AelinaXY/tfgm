package com.tfgm.persistence;

import com.tfgm.models.JourneyTime;
import com.tfgm.persistence.mapper.JourneyTimeMapper;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class JourneyTimeRepoImpl implements JourneyTimeRepo {

  private final JourneyTimeMapper journeyTimeMapper;

  public JourneyTimeRepoImpl(JourneyTimeMapper journeyTimeMapper) {
    this.journeyTimeMapper = journeyTimeMapper;
  }

  @Override
  public void saveJourneyTimes(List<JourneyTime> journeyTimeList) {

    for (JourneyTime journeyTime : journeyTimeList) {
      journeyTimeMapper.saveJourneyTime(journeyTime);
    }
  }

  @Override
  public List<JourneyTime> getAll() {
    return journeyTimeMapper.getAll();
  }

  @Override
  public List<JourneyTime> getDestination(String destination) {
    return journeyTimeMapper.getDestination(destination);
  }
}
