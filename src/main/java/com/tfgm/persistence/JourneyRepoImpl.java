package com.tfgm.persistence;

import com.tfgm.models.Journey;
import com.tfgm.persistence.mapper.JourneyMapper;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class JourneyRepoImpl implements JourneyRepo {

  private final JourneyMapper JMapper;

  public JourneyRepoImpl(JourneyMapper JMapper) {
    this.JMapper = JMapper;
  }

  @Override
  public void saveJourneys(List<Journey> journeyList) {

    if (journeyList != null) {
      for (Journey journey : journeyList) {
        JMapper.create(journey);
      }
    }
  }
}
