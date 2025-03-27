package com.tfgm.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    private static final long FIFTEEN_MINUTES = 900000l;

    private



    @Scheduled(fixedRate = FIFTEEN_MINUTES)
    public void updateJourneyTime()
    {
        tram
    }

}
