package com.tfgm.services;

import com.tfgm.persistence.TramStopRepo;
import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataMongo
@RunWith(SpringRunner.class)
@ActiveProfiles("journeyTests")
public class JourneyRoutingServiceTests {

  @Autowired
  private JourneyRoutingService journeyRoutingService;

  @Test
  public void bartonDockRoadTest() throws IOException {
      String startStop = "St Peter's Square";
      String endStop = "Barton Dock Road";

      Map<String,String> result = journeyRoutingService.findChangeStop(startStop,endStop);


      Assert.assertEquals("StPetersSquareOutgoing",result.get("start"));
      Assert.assertEquals("BartonDockRoadOutgoing",result.get("end"));
      Assert.assertEquals("DeansgateCastlefieldOutgoing",result.get("change"));
  }
}
