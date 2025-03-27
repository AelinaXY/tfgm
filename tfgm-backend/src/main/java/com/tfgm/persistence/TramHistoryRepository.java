package com.tfgm.persistence;

import com.tfgm.models.HistoryStatus;
import com.tfgm.models.TramHistory;
import com.tfgm.persistence.mapper.TramHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

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

    public Set<TramHistory> getNextTramHistorySet() {
        return tramHistoryMapper.getNextTramHistorySet();
    }

    public void updateTramHistoryStatus(UUID tramHistoryId, HistoryStatus status)
    {
        tramHistoryMapper.updateTramHistoryStatus(tramHistoryId, status.name());
    }
}
