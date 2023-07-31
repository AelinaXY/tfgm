package com.tfgm.controllers;

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
    public JourneyController(TramNetworkService service, JourneyRoutingService journeyRoutingService) {
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
    @GetMapping("/calculateNextTrams")
    public ResponseEntity<String> calculateNextTrams(@RequestBody Map<String,String> request) throws IOException {
        System.out.println("requestIn NextTrams:" + Instant.now());
        JSONObject response = journeyRoutingService.calculateNextTrams(request.get("stopname"));
        System.out.println("requestOut JourneyTimes:" + Instant.now());

        if(response == null)
        {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Stop Not Found"
            );
        }

        return ResponseEntity.ok(response.toString());
    }
}
