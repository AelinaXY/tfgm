package com.tfgm.services;

import com.tfgm.models.HistoryStatus;
import com.tfgm.models.JourneyTime;
import com.tfgm.models.TramHistory;
import com.tfgm.persistence.JourneyTimeRepo;
import com.tfgm.persistence.TramHistoryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class JobService {

    private final TramHistoryRepository tramHistoryRepository;
    private final TransactionTemplate transactionTemplate;
    private final JourneyTimeRepo journeyTimeRepo;

    public JobService(TramHistoryRepository tramHistoryRepository, TransactionTemplate transactionTemplate, JourneyTimeRepo journeyTimeRepo) {
        this.tramHistoryRepository = tramHistoryRepository;
        this.transactionTemplate = transactionTemplate;
        this.journeyTimeRepo = journeyTimeRepo;
    }


    @Scheduled(cron = "*/30 * * * * *")
    public void updateJourneyTime() {
        Long time = System.currentTimeMillis();
        System.out.println("Updating journey time");
        Set<TramHistory> tramHistorySet = tramHistoryRepository.getNextTramHistorySet();

        for (TramHistory tramHistory : tramHistorySet) {
            tramHistoryRepository.updateTramHistoryStatus(tramHistory.getTramHistoryId(), HistoryStatus.PROCESSING);

            JourneyTime currentJourneyTime = journeyTimeRepo.getOriginAndDestination(tramHistory.getOrigin(), tramHistory.getDestination());
            long deltaTime = tramHistory.getTimeAtDestination() - tramHistory.getTimeAtOrigin();

            if(currentJourneyTime != null) {
                currentJourneyTime.updateAverage(deltaTime);
            }
            else {
                currentJourneyTime = new JourneyTime(UUID.randomUUID(), tramHistory.getOrigin(), tramHistory.getDestination(), deltaTime, 1L);
            }

            JourneyTime finalCurrentJourneyTime = currentJourneyTime;

            transactionTemplate.executeWithoutResult(callback -> {
                journeyTimeRepo.saveJourneyTimes(List.of(finalCurrentJourneyTime));
                tramHistoryRepository.updateTramHistoryStatus(tramHistory.getTramHistoryId(), HistoryStatus.PROCESSED);
            });
        }
        System.out.println("Finished updating journey time. Took  " + (System.currentTimeMillis() - time) + "ms");

    }

}
