package com.tfgm.persistence;

import com.tfgm.models.Person;

import java.util.List;
import java.util.UUID;

/** */
public interface PersonRepo {
  void savePeople(List<Person> peopleList);


  Person getPerson(UUID uuid);

  void savePerson(Person person);

  List<Person> getAll();
  }
