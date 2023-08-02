package com.tfgm.services;

import com.tfgm.models.*;
import com.tfgm.persistence.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JourneyRoutingService {
  private Map<String, TramStop> tramStopHashMap;

  @Autowired private TramRepo tramRepo;

  @Autowired private JourneyRepo journeyRepo;

  @Autowired private PersonRepo personRepo;

  @Autowired private TramNetworkDTORepo tramNetworkDTORepo;

  @Autowired private TramStopRepo tramStopRepo;

  @Autowired private JourneyTimeRepo journeyTimeRepo;

  public JourneyRoutingService(
      TramRepo tramRepo,
      JourneyRepo journeyRepo,
      PersonRepo personRepo,
      TramNetworkDTORepo tramNetworkDTORepo,
      TramStopRepo tramStopRepo,
      JourneyTimeRepo journeyTimeRepo)
      throws IOException {
    this.tramRepo = tramRepo;
    this.journeyRepo = journeyRepo;
    this.personRepo = personRepo;
    this.tramNetworkDTORepo = tramNetworkDTORepo;
    this.tramStopRepo = tramStopRepo;
    this.journeyTimeRepo = journeyTimeRepo;
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

      Long startTimestamp =
          Long.valueOf(
              ThreadLocalRandom.current()
                  .nextInt(startingTime.intValue(), startingTime.intValue() + 10800));

      personList.add(
          new Person(
              UUID.randomUUID(),
              String.valueOf(i),
              startTimestamp,
              startStop,
              startTimestamp + 3600L,
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

  public void updateJourneyTimes() throws IOException {
    Map<String, TramStop> tramStopHashMap = tramStopRepo.getTramStops();

    List<Tram> tramList = tramRepo.getAll();

    List<JourneyTime> journeyTimeList = new ArrayList<>();

    for (TramStop tramstop : tramStopHashMap.values()) {

      for (TramStopContainer tramStopContainer : tramstop.getNextStops()) {

        journeyTimeList.add(
            new JourneyTime(
                UUID.randomUUID(),
                tramstop.getStopName(),
                tramStopContainer.getTramStop().getStopName()));
      }
    }

    for (Tram tram : tramList) {

      Map<String, Long> tramHistory = tram.getTramHistory();

      for (JourneyTime journeyTime : journeyTimeList) {
        String origin = journeyTime.getOrigin();
        String destination = journeyTime.getDestination();

        // Calculates if the journey exists
        Boolean hasCorrectStops =
            tramHistory.containsKey(origin) && tramHistory.containsKey(destination);

        if (hasCorrectStops && tramHistory.get(origin) < tramHistory.get(destination)) {
          Long absoluteTime = Math.abs(tramHistory.get(origin) - tramHistory.get(destination));

          journeyTime.updateAverage(absoluteTime);
        }
      }
    }

    journeyTimeRepo.saveJourneyTimes(journeyTimeList);

    journeyTimeList = journeyTimeRepo.getAll();

    Collections.sort(
        journeyTimeList,
        (j1, j2) -> {
          return ((int) (j1.getTime() - j2.getTime()));
        });
    System.out.println(journeyTimeList);
  }

  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public JSONObject calculateNextTrams(String stopName, Long timestamp) throws IOException {
    JSONObject responseJSON = new JSONObject();
    List<JSONObject> incomingJSONList = new ArrayList<>();
    List<JSONObject> outgoingJSONList = new ArrayList<>();

    if (stopName != null && timestamp != null) {

      stopName = TramStopServiceUtilities.cleanStationName(stopName);
      Map<String, TramStop> tramStopHashMap = tramStopRepo.getTramStops();

      Long stopRecurseCount = 3L;

      String incomingStopName = stopName + "Incoming";
      String outgoingStopName = stopName + "Outgoing";
      TramStop incomingTramStop = tramStopHashMap.get(incomingStopName);
      TramStop outgoingTramStop = tramStopHashMap.get(outgoingStopName);

      List<JourneyTime> journeyTimeList = journeyTimeRepo.getAll();

      List<Long> timestampList = tramNetworkDTORepo.getAllTimestamps();

      if (incomingTramStop != null) {

        incomingJSONList =
            recursiveTramTimeFinder(
                stopRecurseCount, incomingTramStop, incomingJSONList, journeyTimeList, timestamp);
      }

      if (outgoingTramStop != null) {

        outgoingJSONList =
            recursiveTramTimeFinder(
                stopRecurseCount, outgoingTramStop, outgoingJSONList, journeyTimeList, timestamp);
      }
    }
    responseJSON.put("Incoming", incomingJSONList);
    responseJSON.put("Outgoing", outgoingJSONList);
    System.out.println(responseJSON);
    return responseJSON;
  }

  private List<JSONObject> recursiveTramTimeFinder(
      Long count,
      TramStop tramStop,
      List<JSONObject> returnList,
      List<JourneyTime> journeyTimeList,
      Long timestamp) {
    if (count == 0L || tramStop == null) {
      return returnList;
    }

    count--;

    // Loop through tramStop previous stops
    for (TramStopContainer tramStopContainer : tramStop.getPrevStops()) {
      Long expectedTime =
          Objects.requireNonNull(
                  journeyTimeList.stream()
                      .filter(
                          m ->
                              m.getOrigin().equals(tramStopContainer.getTramStop().getStopName())
                                  && m.getDestination().equals(tramStop.getStopName()))
                      .findFirst()
                      .orElse(null))
              .getTime();
      List<JSONObject> tempList = new ArrayList<>();
      // Add all previous trams to currently working list
      tempList.addAll(
          recursiveTramTimeFinder(
              count,
              tramStopContainer.getTramStop(),
              new ArrayList<>(),
              journeyTimeList,
              timestamp));

      for (Tram tram : tramStopContainer.getTramLinkStop().getTramQueue()) {
        addTramtoReturnList(tempList, timestamp, tram);
      }

      for (JSONObject jsonObject : tempList) {
        Long timeToArrive = jsonObject.getLong("timeToArrival");

        jsonObject.put("timeToArrival", timeToArrive + expectedTime);
      }

      returnList.addAll(tempList);
    }

    for (Tram tram : tramStop.getTramQueue()) {
      addTramtoReturnList(returnList, timestamp, tram);
    }

    // Return returnList with the times updated
    return returnList;
  }

  private static void addTramtoReturnList(List<JSONObject> returnList, Long timestamp, Tram tram) {
    JSONObject tempObject = new JSONObject();
    tempObject.put("timeToArrival", tram.getLastUpdated() - timestamp);
    tempObject.put("endOfLine", tram.getEndOfLine());
    returnList.add(tempObject);
  }

  public Map<String, String> findChangeStop(String startStop, String endStop) throws IOException {

    Map<String, TramStop> tramStopHashMap = tramStopRepo.getTramStops();
    Map<String, String> returnMap = new HashMap<>();

    if (startStop != null && endStop != null) {
      List<TramStop> startTramStops =
          tramStopHashMap.values().stream()
              .filter(tramStop -> tramStop.getStopName().equals(startStop))
              .toList();

      List<TramStop> endTramStops =
          tramStopHashMap.values().stream()
              .filter(tramStop -> tramStop.getStopName().equals(endStop))
              .toList();

      TramStop startTramStopFinal = null;
      TramStop endTramStopFinal = null;

      for (TramStop startTramStop : startTramStops) {
        for (TramStop endTramStop : endTramStops) {
          if (findEndOfLine(endTramStop, startTramStop)) {
            startTramStopFinal = startTramStop;
            endTramStopFinal = endTramStop;
            break;
          }
        }
        if (startTramStopFinal != null) {
          break;
        }
      }

      if (startTramStopFinal == null) {

        for (TramStop startTramStop : startTramStops) {
          for (TramStop endTramStop : endTramStops) {
            List<String> startList = new ArrayList<>();
            List<String> endList = new ArrayList<>();
            listAllNextStop(startTramStop, startList);
            listAllPrevStop(endTramStop, endList);

            if (startList.stream()
                .anyMatch(endList::contains)) {
              startTramStopFinal = startTramStop;
              endTramStopFinal = endTramStop;
              break;
            }
          }
          if (startTramStopFinal != null) {
            break;
          }
        }
      }

      if (startTramStopFinal.getLine().stream().anyMatch(endTramStopFinal.getLine()::contains)) {
        returnMap.put(
            "start",
            TramStopServiceUtilities.cleanStationName(startTramStopFinal.getStopName())
                + startTramStopFinal.getDirection());
        returnMap.put(
            "end",
            TramStopServiceUtilities.cleanStationName(endTramStopFinal.getStopName())
                + endTramStopFinal.getDirection());
        returnMap.put("getOff", "None");
        returnMap.put("getOn", "None");

        return returnMap;
      }

      TramStop changeStation = findChangeStation(endTramStopFinal, startTramStopFinal);

      returnMap.put(
          "start",
          TramStopServiceUtilities.cleanStationName(startTramStopFinal.getStopName())
              + startTramStopFinal.getDirection());
      returnMap.put(
          "end",
          TramStopServiceUtilities.cleanStationName(endTramStopFinal.getStopName())
              + endTramStopFinal.getDirection());
      returnMap.put(
          "getOff",
          TramStopServiceUtilities.cleanStationName(changeStation.getStopName())
              + changeStation.getDirection());

      if (findEndOfLine(endTramStopFinal, changeStation)) {
        returnMap.put(
            "getOn",
            TramStopServiceUtilities.cleanStationName(changeStation.getStopName())
                + changeStation.getDirection());
      } else {
        String otherDirection =
            changeStation.getDirection().equals("Incoming") ? "Outgoing" : "Incoming";
        TramStop switchedDirection =
            tramStopHashMap.get(
                TramStopServiceUtilities.cleanStationName(changeStation.getStopName())
                    + otherDirection);
        returnMap.put(
            "getOn",
            TramStopServiceUtilities.cleanStationName(switchedDirection.getStopName())
                + switchedDirection.getDirection());
      }
    }
    return returnMap;
  }

  private List<String> listAllNextStop(TramStop tramStop, List<String> stopList) {
    if (tramStop.getNextStops() == null) {
      stopList.add(tramStop.getStopName());
      return stopList;
    }
    for (TramStopContainer tramStopContainer : tramStop.getNextStops()) {
      List<String> tempList =
          new ArrayList<>(listAllNextStop(tramStopContainer.getTramStop(), new ArrayList<>()));

      tempList.add(tramStopContainer.getTramStop().getStopName());

      stopList.addAll(tempList);
    }

    return stopList;
  }

  private List<String> listAllPrevStop(TramStop tramStop, List<String> stopList) {
    if (tramStop.getPrevStops() == null) {
      stopList.add(tramStop.getStopName());
      return stopList;
    }
    for (TramStopContainer tramStopContainer : tramStop.getPrevStops()) {
      List<String> tempList =
          new ArrayList<>(listAllPrevStop(tramStopContainer.getTramStop(), new ArrayList<>()));

      tempList.add(tramStopContainer.getTramStop().getStopName());

      stopList.addAll(tempList);
    }

    return stopList;
  }

  private boolean findEndOfLine(TramStop endTramStop, TramStop tramStop) {
    if (tramStop.equals(endTramStop)) {
      return true;
    }

    for (TramStopContainer tramStopContainer : tramStop.getNextStops()) {
      boolean res = findEndOfLine(endTramStop, tramStopContainer.getTramStop());
      if (res) {
        return true;
      }
    }

    return false;
  }

  private TramStop findChangeStation(TramStop endTramStop, TramStop tramStop) {
    if (tramStop.getLine().stream().anyMatch(endTramStop.getLine()::contains)) {
      return tramStop;
    }

    for (TramStopContainer tramStopContainer : tramStop.getNextStops()) {
      TramStop res = findChangeStation(endTramStop, tramStopContainer.getTramStop());
      if (res != null) {
        return res;
      }
    }
    return null;
  }
}
