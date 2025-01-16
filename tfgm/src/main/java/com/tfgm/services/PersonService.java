package com.tfgm.services;

import com.tfgm.models.*;
import com.tfgm.persistence.*;
import java.io.IOException;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

  @Autowired private PersonRepo personRepo;

  public PersonService(PersonRepo personRepo) throws IOException {
    this.personRepo = personRepo;
  }

  public Person addPerson(JSONObject person) {
    Person newPerson = createPerson(person);

    if (newPerson != null) {
      personRepo.savePerson(newPerson);
    }

    return newPerson;
  }

  public List<Person> addPeople(JSONArray people) {
    List<Person> peopleList = new ArrayList<>();

    for (int i = 0; i < people.length(); i++) {
      Person newPerson = createPerson(people.getJSONObject(i));

      if (newPerson != null) {
        peopleList.add(newPerson);
      }
    }

    personRepo.savePeople(peopleList);
    return peopleList;
  }

  private Person createPerson(JSONObject person) {

    if (person != null) {
      Person newPerson = new Person(UUID.randomUUID());

      newPerson.setName(getString(person, "name"));
      newPerson.setTapInTime(getLong(person, "tapInTime"));
      newPerson.setTapInStop(getString(person, "tapInStop"));
      newPerson.setTapOutTime(getLong(person, "tapOutTime"));
      newPerson.setTapOutStop(getString(person, "tapOutStop"));

      return newPerson;
    }

    return null;
  }

  private Long getLong(JSONObject person, String attributeName) {
    try {
      if (person.getLong(attributeName) != 0) {
        return person.getLong(attributeName);
      }
    } catch (JSONException e) {
      System.out.println(attributeName + " ERROR" + e);
    }
    return -1L;
  }

  private String getString(JSONObject person, String attributeName) {
    try {
      if (person.getString(attributeName) != null) {
        return person.getString(attributeName);
      }
    } catch (JSONException e) {
      System.out.println(attributeName + " ERROR" + e);
    }
    return "placeholder";
  }
}
