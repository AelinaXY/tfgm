package com.tfgm.services;

import com.tfgm.models.*;
import com.tfgm.persistence.JourneyRepo;
import com.tfgm.persistence.PersonRepo;
import com.tfgm.persistence.TramNetworkDTORepo;
import com.tfgm.persistence.TramRepo;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JourneyRoutingService {
  private Map<String, TramStop> tramStopHashMap;

  @Autowired private TramRepo tramRepo;

  @Autowired private JourneyRepo journeyRepo;

  @Autowired private PersonRepo personRepo;

  @Autowired private TramNetworkDTORepo tramNetworkDTORepo;

  public JourneyRoutingService(
      TramRepo tramRepo,
      JourneyRepo journeyRepo,
      PersonRepo personRepo,
      TramNetworkDTORepo tramNetworkDTORepo)
      throws IOException {
    this.tramRepo = tramRepo;
    this.journeyRepo = journeyRepo;
    this.personRepo = personRepo;
    this.tramNetworkDTORepo = tramNetworkDTORepo;
  }

  private List<Journey> routeJourney(Person person) {
    Long timestampStart = person.getTapInTime();

    String origin = person.getTapInStop();
    String destination = person.getTapOutStop();

    List<Journey> tramJourneyList = new ArrayList<>();
    List<Tram> originTrams = tramRepo.getInNextTwoHours(timestampStart, origin);

    List<Tram> destinationTrams = tramRepo.getInNextTwoHours(timestampStart, destination);

    for (Tram tram : originTrams) {
      Map<String, Long> tramHistory = tram.getTramHistory();

      if (tramHistory.containsKey(destination)) {
        if (tramHistory.get(destination) > timestampStart) {
          Journey tramJourney =
              new Journey(
                  UUID.randomUUID(),
                  tramHistory.get(origin),
                  origin,
                  tramHistory.get(destination),
                  destination,
                  tram.getUuid(),
                  person.getUuid());
          tramJourneyList.add(tramJourney);
          return tramJourneyList;
        }
      }
    }

    for (Tram originTram : originTrams) {
      Map<String, Long> originTramHistory = originTram.getTramHistory();
      for (Tram destinationTram : destinationTrams) {
        Map<String, Long> destinationTramHistory = destinationTram.getTramHistory();

        String stopName = null;
        Long timestamp = 100000000000L;

        for (String stop : destinationTramHistory.keySet()) {

          if (originTramHistory.containsKey(stop)) {
            if (originTramHistory.get(stop) < timestamp
                && originTramHistory.get(origin) < originTramHistory.get(stop)
                && destinationTramHistory.get(stop) < destinationTramHistory.get(destination)) {
              stopName = stop;
              timestamp = destinationTramHistory.get(stop);
            }
          }
        }

        if (stopName != null) {
          if (originTramHistory.get(stopName) < timestamp) {
            System.out.println(stopName);
            Journey originTramJourney =
                new Journey(
                    UUID.randomUUID(),
                    originTramHistory.get(origin),
                    origin,
                    originTramHistory.get(stopName),
                    stopName,
                    originTram.getUuid(),
                    person.getUuid());
            tramJourneyList.add(originTramJourney);

            Journey destinationTramJourney =
                new Journey(
                    UUID.randomUUID(),
                    destinationTramHistory.get(stopName),
                    stopName,
                    destinationTramHistory.get(destination),
                    destination,
                    destinationTram.getUuid(),
                    person.getUuid());
            tramJourneyList.add(destinationTramJourney);

            return tramJourneyList;
          }
        }
      }
    }

    return null;
  }

  public void tester() {
    System.out.println("Create new Person");
    Person testPerson =
        new Person(
            UUID.randomUUID(), "TestPerson", 1688393998L, "Stretford", 1688398998L, "Firswood");

    System.out.println("Save Person");
    personRepo.savePeople(Arrays.asList(testPerson));

    System.out.println("Getting person");

    Person returnedPerson = personRepo.getPerson(testPerson.getUuid());

    List<Journey> listOfJourneys = routeJourney(returnedPerson);

    journeyRepo.saveJourneys(listOfJourneys);
  }

  public void peoplePopulate() {

    Long startingTime = 1689610800L;

    List<Person> personList = new ArrayList<>();

    List<String> tramStopList =
        Arrays.asList(
            "Abraham Moss",
            "Altrincham",
            "Anchorage",
            "Ashton Moss",
            "Ashton West",
            "Ashton-under-Lyne",
            "Audenshaw",
            "Baguely",
            "Barlow Moor Road",
            "Barton Dock Road",
            "Benchill",
            "Besses o' th' Barn",
            "Bowker Vale",
            "Broadway",
            "Brooklands",
            "Burton Road",
            "Bury",
            "Cemetery Road",
            "Central Park",
            "Chorlton",
            "Clayton Hall",
            "Cornbrook",
            "Crossacres",
            "Crumpsall",
            "Dane Road",
            "Deansgate - Castlefield",
            "Didsbury Village",
            "Droylsden",
            "East Didsbury",
            "Eccles",
            "Edge Lane",
            "Etihad Campus",
            "Exchange Quay",
            "Exchange Square",
            "Failsworth",
            "Firswood",
            "Freehold",
            "Harbour City",
            "Heaton Park",
            "Hollinwood",
            "Holt Town",
            "Imperial War Museum",
            "Kingsway Business Park",
            "Ladywell",
            "Langworthy",
            "Manchester Airport",
            "Market Street",
            "Martinscroft",
            "MediaCityUK",
            "Milnrow",
            "Monsall",
            "Moor Road",
            "Navigation Road",
            "New Islington",
            "Newbold",
            "Newhey",
            "Newton Heath and Moss",
            "Northern Moor",
            "Old Trafford",
            "Oldham Central",
            "Oldham King Street",
            "Oldham Mumps",
            "Parkway",
            "Peel Hall",
            "Piccadilly",
            "Piccadilly Gardens",
            "Pomona",
            "Prestwich",
            "Queens Road",
            "Radcliffe",
            "Robinswood Road",
            "Rochdale Railway Station",
            "Rochdale Town Centre",
            "Roundthorn",
            "Sale",
            "Sale Water Park",
            "Salford Quays",
            "Shadowmoss",
            "Shaw and Crompton",
            "Shudehill",
            "South Chadderton",
            "St Peter's Square",
            "St Werburgh's Road",
            "Stretford",
            "The Trafford Centre",
            "Timperley",
            "Trafford Bar",
            "Velopark",
            "Victoria",
            "Village",
            "Weaste",
            "West Didsbury",
            "Westwood",
            "Wharfside",
            "Whitefield",
            "Withington",
            "Wythenshawe Park",
            "Wythenshawe Town Centre",
            "Derker");

    int randomNum;

    for (int i = 0; i < 40000; i++) {

      //      System.out.println("Created Person number " + i);

      randomNum = ThreadLocalRandom.current().nextInt(0, tramStopList.size());

      String startStop = tramStopList.get(randomNum);

      randomNum = ThreadLocalRandom.current().nextInt(0, tramStopList.size());

      String endStop = tramStopList.get(randomNum);

      Long startTimestamp = Long.valueOf(
          ThreadLocalRandom.current()
              .nextInt(startingTime.intValue(), startingTime.intValue() + 10800));

      personList.add(
          new Person(
              UUID.randomUUID(),
              String.valueOf(i),
              startTimestamp,
              startStop,
              startTimestamp+3600L,
              endStop));
    }

    personRepo.savePeople(personList);
  }

  public void populateJourneys() {
    System.out.println("Start Journey Population");
    List<Person> personList = personRepo.getAll();

    List<Journey> journeyList = new ArrayList<>();

    for (Person person : personList) {
      List<Journey> tempJourney = routeJourney(person);

      if (tempJourney != null) {
        journeyList.addAll(tempJourney);
      }
    }

    journeyRepo.saveJourneys(journeyList);
  }

  public void populateTramNumbers() {
    List<TramNetworkDTO> tramNetworkDTOList = tramNetworkDTORepo.getAll();

    int count = 0;
    for (TramNetworkDTO tramNetworkDTO : tramNetworkDTOList) {
      System.out.println(count);
      count++;
      for (Tram tram : tramNetworkDTO.getTramArrayList()) {
        Long passengers =
            journeyRepo.countPassengers(tram.getUuid(), tramNetworkDTO.getTimestamp());
        tram.setPopulation(passengers);
      }

      tramNetworkDTORepo.saveTramNetworkDTO(tramNetworkDTO);
    }
  }
}
