package com.tfgm.services;

import com.tfgm.models.Journey;
import com.tfgm.models.Tram;
import com.tfgm.models.TramStop;
import com.tfgm.persistence.JourneyRepo;
import com.tfgm.persistence.TramRepo;
import java.io.IOException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JourneyRoutingService {
  private Map<String, TramStop> tramStopHashMap;

  @Autowired private TramRepo tramRepo;

  @Autowired private JourneyRepo journeyRepo;

  public JourneyRoutingService(TramRepo tramRepo, JourneyRepo journeyRepo) throws IOException {
    this.tramRepo = tramRepo;
    this.journeyRepo = journeyRepo;
  }

  private List<Journey> routeJourney(Long timestampStart, String origin, String destination) {
    ArrayList<Journey> tramJourneyList = new ArrayList<>();
    List<Tram> originTrams = tramRepo.getInNextTwoHours(timestampStart, origin);

    List<Tram> destinationTrams = tramRepo.getInNextTwoHours(timestampStart, destination);

    for (Tram tram : originTrams) {
      Map<String, Long> tramHistory = tram.getTramHistory();

      if (tramHistory.containsKey(destination)) {
        Journey tramJourney =
            new Journey(
                UUID.randomUUID(),
                tramHistory.get(origin),
                origin,
                tramHistory.get(destination),
                destination,
                tram.getUuid());
        tramJourneyList.add(tramJourney);
        return tramJourneyList;
      }
    }

    for (Tram originTram : originTrams) {
      Map<String, Long> originTramHistory = originTram.getTramHistory();
      for (Tram destinationTram : destinationTrams) {
        Map<String, Long> destinationTramHistory = destinationTram.getTramHistory();

        String stopName = null;
        Long timestamp = 100000000000L;

        for (String stop : destinationTramHistory.keySet()) {

            if(destinationTramHistory.containsKey("Wharfside"))
            {
            System.out.println("hi");
                if(destinationTramHistory.get("Wharfside") == 1688394701L)
                {
              System.out.println("hi");
                }
            }

          if (originTramHistory.containsKey(stop)) {
            System.out.println(stop);
            if (originTramHistory.get(stop) < timestamp && originTramHistory.get(origin) < originTramHistory.get(stop) && destinationTramHistory.get(stop) < destinationTramHistory.get(destination)){
              stopName = stop;
              timestamp = destinationTramHistory.get(stop);
            }
          }
        }

        System.out.println(stopName);
        System.out.println(timestamp);
        if (stopName != null) {
          if (originTramHistory.get(stopName) < timestamp) {
            Journey originTramJourney =
                new Journey(
                    UUID.randomUUID(),
                    originTramHistory.get(origin),
                    origin,
                    originTramHistory.get(stopName),
                    stopName,
                    originTram.getUuid());
            tramJourneyList.add(originTramJourney);
            Journey destinationTramJourney =
                new Journey(
                    UUID.randomUUID(),
                    destinationTramHistory.get(stopName),
                    stopName,
                    destinationTramHistory.get(destination),
                    destination,
                    destinationTram.getUuid());
            tramJourneyList.add(destinationTramJourney);

            return tramJourneyList;
          }
        }
      }
    }

    return null;
  }

  public void tester() {
    List<Journey> listOfJourneys = routeJourney(1688393998L, "Stretford", "Wharfside");

    journeyRepo.saveJourneys(listOfJourneys);
  }
}
