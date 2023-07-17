package com.tfgm.services;

import com.tfgm.persistence.TramStopRepo;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataMongo
//@RunWith(SpringRunner.class)
public class TramStopGraphServiceTests {

  @Autowired private TramStopRepo tramStopRepo;

  private final TramStopGraphService tramStopGraphService = new TramStopGraphService();

  @BeforeEach
  public void setUp() throws IOException {
  }

  @Test
  public void bartonDockRoadTest() throws IOException {

    System.out.println(tramStopRepo.getTramStops());
  }
}
