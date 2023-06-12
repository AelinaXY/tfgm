package com.tfgm.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trams")
public class TramController {

  @GetMapping("/")
  public ResponseEntity<String> getTrams() {
    return ResponseEntity.ok("ok");
  }
}
