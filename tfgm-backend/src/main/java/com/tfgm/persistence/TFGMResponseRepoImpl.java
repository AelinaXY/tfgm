package com.tfgm.persistence;

import com.tfgm.models.TFGMResponse;
import com.tfgm.models.Tram;
import com.tfgm.models.TramStop;
import com.tfgm.models.TramStopContainer;
import com.tfgm.persistence.mapper.TFGMReponseMapper;
import com.tfgm.persistence.mapper.TramMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class TFGMResponseRepoImpl implements TFGMResponseRepo {

  private final TFGMReponseMapper tfgmReponseMapper;

  public TFGMResponseRepoImpl(TFGMReponseMapper tfgmReponseMapper) {
    this.tfgmReponseMapper = tfgmReponseMapper;
  }


    @Override
    public void saveResponse(TFGMResponse response) {
      tfgmReponseMapper.saveResponse(response);
    }

    @Override
    public List<TFGMResponse> getAllResponse() {
        return tfgmReponseMapper.getAll();
    }
}
