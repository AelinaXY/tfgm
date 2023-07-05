package com.tfgm.persistence;

import com.tfgm.models.Journey;
import com.tfgm.models.Person;
import com.tfgm.persistence.mapper.JourneyMapper;
import java.util.List;
import java.util.UUID;

import com.tfgm.persistence.mapper.PersonMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepoImpl implements PersonRepo {

  private final PersonMapper PMapper;

  public PersonRepoImpl(PersonMapper PMapper) {
    this.PMapper = PMapper;
  }
    @Override
    public void savePeople(List<Person> peopleList) {
      int i = 0;

        if (peopleList != null) {
            for (Person person : peopleList) {
//        System.out.println("Saved person number" + i);
                PMapper.create(person);
                i++;
            }
        }

    }

    @Override
    public Person getPerson(UUID uuid) {
        return PMapper.get(uuid);
    }

    @Override
    public List<Person> getAll() {
        return PMapper.getAll();
    }
}
