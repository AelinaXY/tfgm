package com.tfgm.persistence;

import com.tfgm.models.TFGMResponse;
import com.tfgm.models.Tram;
import com.tfgm.models.TramStop;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** */
public interface TFGMResponseRepo {
  void saveResponse(TFGMResponse response);

  List<TFGMResponse> getAllResponse();
}
