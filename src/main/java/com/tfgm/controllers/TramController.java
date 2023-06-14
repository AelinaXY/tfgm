package com.tfgm.controllers;

import com.tfgm.services.TramNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trams")
public class TramController {

  @Autowired TramNetworkService service;

  public TramController(TramNetworkService service) {
    this.service = service;
  }

  @CrossOrigin
  @GetMapping("/")
  public ResponseEntity<String> getTrams() {
    return ResponseEntity.ok(service.getAllTrams().toString());
  }

  @CrossOrigin
  @GetMapping("/timestamp/{timestamp}")
  public ResponseEntity<String> getTramAtTime(@PathVariable Long timestamp) {
    return ResponseEntity.ok(service.getByTimestamp(Long.valueOf(timestamp)).toJSONString());
  }

  @CrossOrigin
  @GetMapping("/timestamps")
  public ResponseEntity<String> getTramAtTime() {
    return ResponseEntity.ok(service.getAllTimestamps().toString());
  }

  @CrossOrigin
  @GetMapping("/{tramStopName}")
  public ResponseEntity<String> getAllTramsAt(@PathVariable String tramStopName) {
    return ResponseEntity.ok(service.getAllTramsAt(tramStopName).toString());
  }
}
