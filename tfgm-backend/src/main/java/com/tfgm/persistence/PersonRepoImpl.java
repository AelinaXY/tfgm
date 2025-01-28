package com.tfgm.persistence;

import com.tfgm.models.Person;
import com.tfgm.persistence.mapper.PersonMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepoImpl implements PersonRepo {

  private final PersonMapper personMapper;

  public PersonRepoImpl(PersonMapper personMapper) {
    this.personMapper = personMapper;
  }

  @Override
  public void savePeople(List<Person> peopleList) {
    int i = 0;

    if (peopleList != null) {
      for (Person person : peopleList) {
        personMapper.create(person);
      }
    }
  }

  @Override
  public void savePerson(Person person) {
    personMapper.create(person);
  }

  @Override
  public Person getPerson(UUID uuid) {
    return personMapper.get(uuid);
  }

  @Override
  public List<Person> getAll() {
    return personMapper.getAll();
  }
}
