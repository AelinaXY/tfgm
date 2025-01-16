package com.tfgm.controllers;

import com.tfgm.models.JourneyPlan;
import com.tfgm.models.TramJourneyResponse;
import com.tfgm.services.JourneyRoutingService;
import com.tfgm.services.TramNetworkService;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/journey")
public class JourneyController {
  TramNetworkService service;

  JourneyRoutingService journeyRoutingService;

  @Autowired
  public JourneyController(
      TramNetworkService service, JourneyRoutingService journeyRoutingService) {
    this.service = service;
    this.journeyRoutingService = journeyRoutingService;
  }

  @CrossOrigin
  @GetMapping("/journeyPing")
  public void testing() {
    System.out.println("requestIn TEST:" + Instant.now());
    journeyRoutingService.tester();
    System.out.println("requestOut TEST:" + Instant.now());
  }

  @CrossOrigin
  @GetMapping("/makePeople")
  public void makePeople() {
    System.out.println("requestIn People:" + Instant.now());
    journeyRoutingService.peoplePopulate();
    System.out.println("requestOut People:" + Instant.now());
  }

  @CrossOrigin
  @GetMapping("/makeJourney")
  public void makeJourney() {
    System.out.println("requestIn Journey:" + Instant.now());
    journeyRoutingService.populateJourneys();
    System.out.println("requestOut Journey:" + Instant.now());
  }

  @CrossOrigin
  @GetMapping("/updateNetwork")
  public void updateFullNetwork() {
    journeyRoutingService.populateJourneys();
    journeyRoutingService.populateTramNumbers();
  }

  @CrossOrigin
  @GetMapping("/updateTramNetworkDTO")
  public void updateTramNetworkDTO() {
    System.out.println("requestIn TramNetwork:" + Instant.now());
    journeyRoutingService.populateTramNumbers();
    System.out.println("requestOut TramNetwork:" + Instant.now());
  }

  @CrossOrigin
  @GetMapping("/updateJourneyTimes")
  public void updateJourneyTimes() throws IOException {
    System.out.println("requestIn JourneyTimes:" + Instant.now());
    journeyRoutingService.updateJourneyTimes();
    System.out.println("requestOut JourneyTimes:" + Instant.now());
  }

  @CrossOrigin
  @PostMapping("/calculateNextTrams")
  public ResponseEntity<String> calculateNextTrams(@RequestBody Map<String, String> request)
      throws IOException {
    System.out.println("requestIn NextTrams:" + Instant.now());
    JSONObject response =
        journeyRoutingService.calculateNextTrams(
            request.get("stopname"), Long.valueOf(request.get("timestamp")));
    System.out.println("requestOut JourneyTimes:" + Instant.now());

    if (response == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stop Not Found");
    }

    return ResponseEntity.ok(response.toString());
  }

  // 1689799662

  @CrossOrigin
  @PostMapping("/calculateJourney")
  public ResponseEntity<String> calculateJourneyTest(@RequestBody Map<String, String> request)
      throws IOException {
    JourneyPlan journeyPlan =
        journeyRoutingService.findChangeStop(request.get("startStop"), request.get("endStop"));
    TramJourneyResponse response =
        journeyRoutingService.findJourneyPlan(
            journeyPlan.getStart(),
            journeyPlan.getEnd(),
            journeyPlan.getFirstGetOff(),
            journeyPlan.getFirstGetOn(),
            journeyPlan.getSecondGetOff(),
            journeyPlan.getSecondGetOn(),
            Long.valueOf(request.get("timestamp")));

    if (response == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, request.toString());
    }
    response.setFirstChangeStop(journeyPlan.getFirstChangeLegal());
    response.setSecondChangeStop(journeyPlan.getSecondChangeLegal());

    return ResponseEntity.ok(new JSONObject(response).toString());
  }
}
