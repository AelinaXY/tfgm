package com.tfgm.persistence;

import com.tfgm.models.Journey;
import com.tfgm.persistence.mapper.JourneyMapper;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class JourneyRepoImpl implements JourneyRepo {

  private final JourneyMapper JMapper;

  public JourneyRepoImpl(JourneyMapper JMapper) {
    this.JMapper = JMapper;
  }

  @Override
  public void saveJourneys(List<Journey> journeyList) {
    int i = 0;

    if (journeyList != null) {
      for (Journey journey : journeyList) {
        System.out.println("Saved Journey " + i);
        i++;
        JMapper.create(journey);
      }
    }
  }

    @Override
    public long countPassengers(UUID tramUUID, Long timestamp) {
        return JMapper.countPassengers(tramUUID,timestamp);
    }
}
