package com.tfgm.persistence;

import com.tfgm.models.TramHistory;
import com.tfgm.persistence.mapper.TramHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TramHistoryRepository {

    @Autowired
    private final TramHistoryMapper tramHistoryMapper;

    public TramHistoryRepository(TramHistoryMapper tramHistoryMapper) {
        this.tramHistoryMapper = tramHistoryMapper;
    }

    public void saveTramHistory(TramHistory tramHistory) {
        tramHistoryMapper.create(tramHistory);
    }
}
