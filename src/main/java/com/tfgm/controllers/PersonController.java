package com.tfgm.controllers;

import com.tfgm.services.PersonService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/person")
public class PersonController {
  PersonService service;

  @Autowired
  public PersonController(PersonService service) {
    this.service = service;
  }

  @CrossOrigin
  @PostMapping("/")
  public ResponseEntity<String> addPerson(@RequestBody String personObject) {
    JSONObject request = new JSONObject(personObject);
    return ResponseEntity.ok(service.addPerson(request).toString());
  }

  @CrossOrigin
  @PostMapping("/addPeople")
  public ResponseEntity<String> addPeople(@RequestBody String personArray) {
    JSONArray request = new JSONArray(personArray);
    return ResponseEntity.ok(service.addPeople(request).toString());
  }
}
