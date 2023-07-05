package com.tfgm.persistence;

import com.tfgm.models.Person;

import java.util.List;
import java.util.UUID;

import com.tfgm.persistence.mapper.PersonMapper;
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
//        System.out.println("Saved person number" + i);
                personMapper.create(person);
                i++;
            }
        }

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
