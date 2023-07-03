package com.tfgm.controllers;

import com.tfgm.services.JourneyRoutingService;
import com.tfgm.services.TramNetworkService;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trams")
public class TramController {

  @Autowired TramNetworkService service;

  @Autowired JourneyRoutingService journeyRoutingService;

  public TramController(TramNetworkService service, JourneyRoutingService journeyRoutingService) {
    this.service = service;
    this.journeyRoutingService = journeyRoutingService;
  }

  @CrossOrigin
  @GetMapping("/")
  public ResponseEntity<String> getTrams() {

    return ResponseEntity.ok(
        service.getAllTrams().stream().map(m -> m.toJSONString()).toList().toString());
  }

  @CrossOrigin
  @GetMapping("/timestamp/{timestamp}")
  public ResponseEntity<String> getTramAtTime(@PathVariable Long timestamp) {
    return ResponseEntity.ok(service.getByTimestamp(Long.valueOf(timestamp)).toJSONString());
  }

  @CrossOrigin
  @GetMapping("/timestamps")
  public ResponseEntity<String> getAllTimestamps() {
    return ResponseEntity.ok(service.getAllTimestamps().toString());
  }
  //
  //  @CrossOrigin
  //  @GetMapping("/{tramStopName}")
  //  public ResponseEntity<String> getAllTramsAt(@PathVariable String tramStopName) {
  //    return ResponseEntity.ok(service.getAllTramsAt(tramStopName).toString());
  //  }
  //
  @CrossOrigin
  @GetMapping("/alltramsatstop/{timestamp}")
  public ResponseEntity<String> getAllTramsAtAllStops(@PathVariable Long timestamp) {
    System.out.println("requestIn:" + Instant.now());
    String response = service.getAllTramsAtTimestamp(timestamp);
    System.out.println("requestOut:" + Instant.now());
    return ResponseEntity.ok(response);
  }

  @CrossOrigin
  @GetMapping("/journeyPing")
  public void testing() {
    System.out.println("requestIn TEST:" + Instant.now());
    journeyRoutingService.tester();
    System.out.println("requestOut TEST:" + Instant.now());
  }
}
