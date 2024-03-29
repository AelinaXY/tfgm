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

      if (incomingTramStop != null) {

        incomingJSONList =
            recursiveTramTimeFinder(
                stopRecurseCount, incomingTramStop, incomingJSONList, journeyTimeList, timestamp);

        List<JSONObject> removalList =
            getRemovalList(incomingJSONList, tramStopHashMap, incomingTramStop);

        incomingJSONList.removeAll(removalList);
      }

      if (outgoingTramStop != null) {

        outgoingJSONList =
            recursiveTramTimeFinder(
                stopRecurseCount, outgoingTramStop, outgoingJSONList, journeyTimeList, timestamp);

        List<JSONObject> removalList =
            getRemovalList(outgoingJSONList, tramStopHashMap, outgoingTramStop);

        outgoingJSONList.removeAll(removalList);
      }
    }
    responseJSON.put("Incoming", incomingJSONList);
    responseJSON.put("Outgoing", outgoingJSONList);
    System.out.println(responseJSON);
    return responseJSON;
  }

  private List<JSONObject> getRemovalList(
      List<JSONObject> incomingJSONList,
      Map<String, TramStop> tramStopHashMap,
      TramStop incomingTramStop) {
    List<JSONObject> removalList = new ArrayList<>();

    for (JSONObject jsonObject : incomingJSONList) {
      Tram currentTram = (Tram) jsonObject.get("tramObject");
      boolean avoidExchangeSquare;

      avoidExchangeSquare =
          !jsonObject
              .getString("endOfLine")
              .matches("East Didsbury|Shaw and Crompton|Rochdale Town Centre");

      List<TramStop> stopsBetweenTemp = new ArrayList<>();

      String endOfLineOutgoing =
          TramStopServiceUtilities.cleanStationName(jsonObject.getString("endOfLine")) + "Outgoing";
      String endOfLineIncoming =
          TramStopServiceUtilities.cleanStationName(jsonObject.getString("endOfLine")) + "Incoming";

      String startStopName = currentTram.getOrigin();

      findAllStopsBetween(
          tramStopHashMap.get(endOfLineOutgoing),
          tramStopHashMap.get(startStopName),
          stopsBetweenTemp,
          avoidExchangeSquare);

      if (stopsBetweenTemp.size() == 0) {
        stopsBetweenTemp = new ArrayList<>();
        findAllStopsBetween(
            tramStopHashMap.get(endOfLineIncoming),
            tramStopHashMap.get(startStopName),
            stopsBetweenTemp,
            avoidExchangeSquare);
      }

      if (!stopsBetweenTemp.contains(incomingTramStop)) {
        removalList.add(jsonObject);
      }
    }
    return removalList;
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
        // if prime tramstop is not between tramstop and endofline tramstop
        // don't add to list
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
    tempObject.put("endOfLine", TramStopGraphService.getCorrectEndOfLine(tram));
    tempObject.put("endOfLineDisplay", tram.getEndOfLine());

    tempObject.put("tramObject", tram);
    returnList.add(tempObject);
  }

  public JourneyPlan findChangeStop(String startStop, String endStop) throws IOException {

    Map<String, TramStop> tramStopHashMap = tramStopRepo.getTramStops();

    JourneyPlan returnPlan = new JourneyPlan();

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

            if (startList.stream().anyMatch(endList::contains)) {
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

      if (startTramStopFinal == null) {

          JourneyPlan newResultPlan = getEcclesJourneyPlan(startStop, endStop);
          if (newResultPlan != null) return newResultPlan;
      }

      if (startTramStopFinal.getLine().stream().anyMatch(endTramStopFinal.getLine()::contains)) {
        returnPlan.setStart(
            TramStopServiceUtilities.cleanStationName(startTramStopFinal.getStopName())
                + startTramStopFinal.getDirection());
        returnPlan.setEnd(
            TramStopServiceUtilities.cleanStationName(endTramStopFinal.getStopName())
                + endTramStopFinal.getDirection());

        return returnPlan;
      }

      TramStop changeStation =
          findChangeStation(endTramStopFinal, startTramStopFinal, startTramStopFinal);

      if(changeStation == null)
      {
          JourneyPlan newResultPlan = getEcclesJourneyPlan(startStop, endStop);
          if (newResultPlan != null) return newResultPlan;
      }

      returnPlan.setStart(
          TramStopServiceUtilities.cleanStationName(startTramStopFinal.getStopName())
              + startTramStopFinal.getDirection());
      returnPlan.setEnd(
          TramStopServiceUtilities.cleanStationName(endTramStopFinal.getStopName())
              + endTramStopFinal.getDirection());
      returnPlan.setFirstGetOff(
          TramStopServiceUtilities.cleanStationName(changeStation.getStopName())
              + changeStation.getDirection());

      returnPlan.setFirstChangeLegal(changeStation.getStopName());

      if (findEndOfLine(endTramStopFinal, changeStation)) {
        returnPlan.setFirstGetOn(
            TramStopServiceUtilities.cleanStationName(changeStation.getStopName())
                + changeStation.getDirection());
      } else {
        String otherDirection =
            changeStation.getDirection().equals("Incoming") ? "Outgoing" : "Incoming";
        TramStop switchedDirection =
            tramStopHashMap.get(
                TramStopServiceUtilities.cleanStationName(changeStation.getStopName())
                    + otherDirection);
        returnPlan.setFirstGetOn(
            TramStopServiceUtilities.cleanStationName(switchedDirection.getStopName())
                + switchedDirection.getDirection());
      }
    }
    return returnPlan;
  }

    private JourneyPlan getEcclesJourneyPlan(String startStop, String endStop) throws IOException {
        String ecclesSpurStops = "Eccles|Ladywell|Weaste|Langworthy|Broadway";

        if (startStop.matches(ecclesSpurStops)) {
          JourneyPlan newResultPlan = findChangeStop("MediaCityUK", endStop);
          newResultPlan.setSecondChangeLegal(newResultPlan.getFirstChangeLegal());
          newResultPlan.setSecondGetOn(newResultPlan.getFirstGetOn());
          newResultPlan.setSecondGetOff(newResultPlan.getFirstGetOff());
          newResultPlan.setFirstGetOff("MediaCityUKOutgoing");
          newResultPlan.setFirstGetOn(newResultPlan.getStart());
          newResultPlan.setFirstChangeLegal("MediaCityUK");
          newResultPlan.setStart(startStop + "Incoming");

            return newResultPlan;
        }

        if (endStop.matches(ecclesSpurStops)) {
          JourneyPlan newResultPlan = findChangeStop(startStop, "MediaCityUK");

          newResultPlan.setSecondGetOff(newResultPlan.getEnd());
          newResultPlan.setSecondGetOn("MediaCityUKIncoming");
          newResultPlan.setSecondChangeLegal("MediaCityUK");
          newResultPlan.setEnd(endStop + "Outgoing");
            return newResultPlan;
        }
        return null;
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

  private TramStop findChangeStation(TramStop endTramStop, TramStop tramStop, TramStop startStop) {
    if (tramStop.getLine().stream().anyMatch(endTramStop.getLine()::contains)
        && tramStop.getLine().stream().anyMatch(startStop.getLine()::contains)) {
      return tramStop;
    }

    for (TramStopContainer tramStopContainer : tramStop.getNextStops()) {
      TramStop res = findChangeStation(endTramStop, tramStopContainer.getTramStop(), startStop);
      if (res != null) {
        return res;
      }
    }
    return null;
  }

  public TramJourneyResponse findJourneyPlan(
      String startStop,
      String endStop,
      String firstChangeStopOff,
      String firstChangeStopOn,
      String secondChangeStopOff,
      String secondChangeStopOn,
      Long timestamp)
      throws IOException {

    List<JourneyTime> journeyTimeList = journeyTimeRepo.getAll();

    Map<String, TramStop> tramStopHashMap = tramStopRepo.getTramStops();

    Long stopRecurseCount = 8L;

    TramStop startStopObject = tramStopHashMap.get(startStop);
    TramStop endStopObject = tramStopHashMap.get(endStop);

    // Get list of all trams arriving at start stop
    if (!secondChangeStopOff.equals("None")) {
      TramStop firstChangeStopOffObject = tramStopHashMap.get(firstChangeStopOff);
      TramStop firstChangeStopOnObject = tramStopHashMap.get(firstChangeStopOn);
      TramStop secondChangeStopOffObject = tramStopHashMap.get(secondChangeStopOff);
      TramStop secondChangeStopOnObject = tramStopHashMap.get(secondChangeStopOn);

      TramJourneyResponse returnJourneyResponseFirst =
          getTramJourneyResponse(
              timestamp,
              journeyTimeList,
              tramStopHashMap,
              startStopObject,
              firstChangeStopOffObject);

      if (returnJourneyResponseFirst != null) {
        TramJourneyResponse returnJourneyResponseSecond =
            getTramJourneyResponse(
                timestamp
                    + returnJourneyResponseFirst.getJourneyLength()
                    + returnJourneyResponseFirst.getFirstTramArrivalTime(),
                journeyTimeList,
                tramStopHashMap,
                firstChangeStopOnObject,
                secondChangeStopOffObject);

        if (returnJourneyResponseSecond != null) {

          TramJourneyResponse returnJourneyResponseThird =
              getTramJourneyResponse(
                  timestamp
                      + returnJourneyResponseFirst.getJourneyLength()
                      + returnJourneyResponseFirst.getFirstTramArrivalTime()
                      + returnJourneyResponseSecond.getJourneyLength()
                      + returnJourneyResponseSecond.getFirstTramArrivalTime(),
                  journeyTimeList,
                  tramStopHashMap,
                  secondChangeStopOnObject,
                  endStopObject);

          if (returnJourneyResponseThird != null) {

            returnJourneyResponseFirst.setSecondTramArrivalTime(
                returnJourneyResponseSecond.getFirstTramArrivalTime());

            returnJourneyResponseFirst.setThirdTramArrivalTime(
                returnJourneyResponseThird.getFirstTramArrivalTime());

            returnJourneyResponseFirst.setJourneyLength(
                returnJourneyResponseFirst.getJourneyLength()
                    + returnJourneyResponseSecond.getJourneyLength()
                    + returnJourneyResponseThird.getJourneyLength()
                    + returnJourneyResponseFirst.getFirstTramArrivalTime()
                    + returnJourneyResponseFirst.getSecondTramArrivalTime()
                    + returnJourneyResponseFirst.getThirdTramArrivalTime());

            returnJourneyResponseFirst.setSecondTram(returnJourneyResponseSecond.getFirstTram());

            returnJourneyResponseFirst.setThirdTram(returnJourneyResponseThird.getFirstTram());

            return returnJourneyResponseFirst;
          }
        }
      }

    } else {
      if (!firstChangeStopOff.equals("None")) {
        TramStop changeStopOffObject = tramStopHashMap.get(firstChangeStopOff);
        TramStop changeStopOnObject = tramStopHashMap.get(firstChangeStopOn);

        TramJourneyResponse returnJourneyResponseFirst =
            getTramJourneyResponse(
                timestamp, journeyTimeList, tramStopHashMap, startStopObject, changeStopOffObject);

        if (returnJourneyResponseFirst != null) {
          TramJourneyResponse returnJourneyResponseSecond =
              getTramJourneyResponse(
                  timestamp
                      + returnJourneyResponseFirst.getJourneyLength()
                      + returnJourneyResponseFirst.getFirstTramArrivalTime(),
                  journeyTimeList,
                  tramStopHashMap,
                  changeStopOnObject,
                  endStopObject);

          if (returnJourneyResponseSecond != null) {

            returnJourneyResponseFirst.setSecondTramArrivalTime(
                returnJourneyResponseSecond.getFirstTramArrivalTime());

            returnJourneyResponseFirst.setJourneyLength(
                returnJourneyResponseFirst.getJourneyLength()
                    + returnJourneyResponseSecond.getJourneyLength()
                    + returnJourneyResponseFirst.getFirstTramArrivalTime()
                    + returnJourneyResponseFirst.getSecondTramArrivalTime());

            returnJourneyResponseFirst.setSecondTram(returnJourneyResponseSecond.getFirstTram());

            return returnJourneyResponseFirst;
          }
        }

      } else {

        TramJourneyResponse returnJourneyResponse =
            getTramJourneyResponse(
                timestamp, journeyTimeList, tramStopHashMap, startStopObject, endStopObject);
        if (returnJourneyResponse != null) {
          returnJourneyResponse.setJourneyLength(
              returnJourneyResponse.getJourneyLength()
                  + returnJourneyResponse.getFirstTramArrivalTime());
          return returnJourneyResponse;
        }
      }
    }

    // Get list of all trams arriving at change stop
    //
    return null;
  }

  private TramJourneyResponse getTramJourneyResponse(
      Long timestamp,
      List<JourneyTime> journeyTimeList,
      Map<String, TramStop> tramStopHashMap,
      TramStop startStopObject,
      TramStop endStopObject) {
    List<JSONObject> startJSONList = new ArrayList<>();

    Long stopRecurseCount = 8L;

    startJSONList =
        recursiveTramTimeFinder(
            stopRecurseCount, startStopObject, startJSONList, journeyTimeList, timestamp);

    startJSONList.sort(new TramArrivalListComparator());

    startJSONList = startJSONList.stream().filter(m -> (Long) m.get("timeToArrival") > 0).toList();

    System.out.println(startJSONList.stream().map(m -> m.get("endOfLine")).toList());

    List<TramStop> stopsBetween = new ArrayList<>();

    boolean avoidExchangeSquare;

    if (checkExchangeSquare(startStopObject) || checkExchangeSquare(endStopObject)) {
      avoidExchangeSquare = false;
    } else {
      avoidExchangeSquare = true;
    }

    findAllStopsBetween(endStopObject, startStopObject, stopsBetween, avoidExchangeSquare);

    // Part One: Check tram data from the graph
    for (JSONObject jsonObject : startJSONList) {
      List<TramStop> stopsBetweenTemp = new ArrayList<>();
      System.out.println(
          TramStopServiceUtilities.cleanStationName(jsonObject.getString("endOfLine"))
              + "Outgoing");

      if (jsonObject
          .getString("endOfLine")
          .matches("East Didsbury|Shaw and Crompton|Rochdale Town Centre")) {
        avoidExchangeSquare = false;
      } else {
        avoidExchangeSquare = true;
      }

      findAllStopsBetween(
          tramStopHashMap.get(
              TramStopServiceUtilities.cleanStationName(jsonObject.getString("endOfLine"))
                  + "Outgoing"),
          startStopObject,
          stopsBetweenTemp,
          avoidExchangeSquare);

      if (stopsBetweenTemp.size() == 0) {
        stopsBetweenTemp = new ArrayList<>();
        findAllStopsBetween(
            tramStopHashMap.get(
                TramStopServiceUtilities.cleanStationName(jsonObject.getString("endOfLine"))
                    + "Incoming"),
            startStopObject,
            stopsBetweenTemp,
            avoidExchangeSquare);
      }

      // Find all stops between end of line and current stop
      if (stopsBetweenTemp != null) {
        if (stopsBetweenTemp.contains(endStopObject)) {
          Long count = getJourneyLength(journeyTimeList, stopsBetween);

          TramJourneyResponse returnJourneyResponse = new TramJourneyResponse();

          returnJourneyResponse.setJourneyLength(count);
          returnJourneyResponse.setFirstTram((Tram) jsonObject.get("tramObject"));
          returnJourneyResponse.setFirstTramArrivalTime(jsonObject.getLong("timeToArrival"));
          return returnJourneyResponse;
        }
      }
    }

    // Part Two: Check past TFGM responses

    // Find closest tfgm response if it is within 30s of current timestamp

    // Get Stop line
    // Check if any of the trams due go to the destination
    // If yes return that timestamp
    // If no continue

    // Part Three: Check past tram times

    Long timeBetweenTramsAvg =
        tramRepo.getTimeBetweenTramsAvg(startStopObject.getStopName(), endStopObject.getStopName());

    if (timeBetweenTramsAvg != null) {

      Long count = getJourneyLength(journeyTimeList, stopsBetween);

      Long lastUpdate =
          tramRepo.getLastTimeAtStop(startStopObject.getStopName(), endStopObject.getStopName());

      Long timeTilTram = lastUpdate - timestamp + timeBetweenTramsAvg;

      while (timeTilTram <= 0L) {
        timeTilTram += timeBetweenTramsAvg;
      }

      TramJourneyResponse returnJourneyResponse = new TramJourneyResponse();

      returnJourneyResponse.setJourneyLength(count);
      returnJourneyResponse.setFirstTram(null);
      returnJourneyResponse.setFirstTramArrivalTime(timeTilTram);
      return returnJourneyResponse;
    }

    return null;
  }

  private static Long getJourneyLength(
      List<JourneyTime> journeyTimeList, List<TramStop> stopsBetween) {
    Long count = 0L;
    Collections.reverse(stopsBetween);

    for (int i = 1; i < stopsBetween.size(); i++) {
      int finalI = i;
      count +=
          Objects.requireNonNull(
                  journeyTimeList.stream()
                      .filter(
                          m ->
                              m.getDestination().equals(stopsBetween.get(finalI).getStopName())
                                  && m.getOrigin()
                                      .equals(stopsBetween.get(finalI - 1).getStopName()))
                      .findFirst()
                      .orElse(null))
              .getTime();
    }
    return count;
  }

  private boolean checkExchangeSquare(TramStop tramStop) {
    List<String> lineList = tramStop.getLine();
    boolean atEndOfLine = lineList.contains("Didsbury-Rochdale") && lineList.size() == 1;
    boolean otherPlaces =
        lineList.contains("Didsbury-Rochdale")
            && lineList.contains("Didsbury-ShawAndCrompton")
            && lineList.size() == 2;

    return atEndOfLine || otherPlaces;
  }

  private List<TramStop> findAllStopsBetween(
      TramStop endTramStop,
      TramStop tramStop,
      List<TramStop> stopBetweenList,
      Boolean avoidExchangeSquare) {
    if (tramStop.equals(endTramStop)) {
      stopBetweenList.add(tramStop);
      return stopBetweenList;
    }
    List<TramStop> tempList = null;

    for (TramStopContainer tramStopContainer : tramStop.getNextStops()) {

      if (!(tramStopContainer.getTramStop().getStopName().equals("Exchange Square")
          && avoidExchangeSquare)) {
        tempList =
            findAllStopsBetween(
                endTramStop, tramStopContainer.getTramStop(), stopBetweenList, avoidExchangeSquare);
        if (tempList != null) {
          break;
        }
      }
    }
    if (tempList != null) {
      stopBetweenList.add(tramStop);
      return stopBetweenList;
    } else {
      return null;
    }
  }

  static class TramArrivalListComparator implements Comparator<JSONObject> {
    @Override
    public int compare(JSONObject o1, JSONObject o2) {
      return Math.toIntExact((Long) o1.get("timeToArrival") - (Long) o2.get("timeToArrival"));
    }
  }
}
