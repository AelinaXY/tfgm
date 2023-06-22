package com.tfgm.persistence;

import com.tfgm.models.TramStop;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface TramNetworkDTORepoCustom {
    public void saveTramNetwork(Map<String, TramStop> tramStopHashMap);
}
