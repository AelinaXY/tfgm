package com.tfgm.services;

import java.io.IOException;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

// @ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataMongo
@RunWith(SpringRunner.class)
@ActiveProfiles("journeyTests")
public class JourneyRoutingServiceTests {

  @Autowired private JourneyRoutingService journeyRoutingService;

  @Test
  public void journeyRoutingSimpleChangeTest() throws IOException {
    String startStop = "St Peter's Square";
    String endStop = "Barton Dock Road";

    Map<String, String> result = journeyRoutingService.findChangeStop(startStop, endStop);

    Assert.assertEquals("StPetersSquareOutgoing", result.get("start"));
    Assert.assertEquals("BartonDockRoadOutgoing", result.get("end"));
    Assert.assertEquals("DeansgateCastlefieldOutgoing", result.get("getOff"));
    Assert.assertEquals("DeansgateCastlefieldOutgoing", result.get("getOn"));
  }

  // Tests the need to change direction
  @Test
  public void journeyRoutingComplexChangeTest() throws IOException {
    String startStop = "Monsall";
    String endStop = "Edge Lane";

    Map<String, String> result = journeyRoutingService.findChangeStop(startStop, endStop);

    Assert.assertEquals("MonsallIncoming", result.get("start"));
    Assert.assertEquals("EdgeLaneOutgoing", result.get("end"));
    Assert.assertEquals("StPetersSquareOutgoing", result.get("getOff"));
    Assert.assertEquals("StPetersSquareIncoming", result.get("getOn"));
  }

  // Tests center of Manchester
  @Test
  public void journeyRoutingExchangeSquareChangeTest() throws IOException {
    String startStop = "Market Street";
    String endStop = "Exchange Square";

    Map<String, String> result = journeyRoutingService.findChangeStop(startStop, endStop);

    Assert.assertEquals("MarketStreetOutgoing", result.get("start"));
    Assert.assertEquals("ExchangeSquareIncoming", result.get("end"));
    Assert.assertEquals("VictoriaOutgoing", result.get("getOff"));
    Assert.assertEquals("VictoriaIncoming", result.get("getOn"));
  }

  @Test
  public void journeyRoutingMediaCityChangeTest() throws IOException {
    String startStop = "Navigation Road";
    String endStop = "Eccles";

    Map<String, String> result = journeyRoutingService.findChangeStop(startStop, endStop);

    Assert.assertEquals("NavigationRoadIncoming", result.get("start"));
    Assert.assertEquals("EcclesOutgoing", result.get("end"));
    Assert.assertEquals("CornbrookIncoming", result.get("getOff"));
    Assert.assertEquals("CornbrookOutgoing", result.get("getOn"));
  }

  @Test
  public void journeyRoutingNoChangeTest() throws IOException {
    String startStop = "Navigation Road";
    String endStop = "Besses O' Th' Barn";

    Map<String, String> result = journeyRoutingService.findChangeStop(startStop, endStop);

    Assert.assertEquals("NavigationRoadIncoming", result.get("start"));
    Assert.assertEquals("BessesOThBarnOutgoing", result.get("end"));
    Assert.assertEquals("None", result.get("getOn"));
    Assert.assertEquals("None", result.get("getOff"));
  }

  @Test
  public void journeyRoutingBadInputTest() throws IOException {
    String startStop = null;
    String endStop = "I AM AN INVALID TRAM STOP";

    Map<String, String> result = journeyRoutingService.findChangeStop(startStop, endStop);

    Assert.assertNull(result.get("start"));
    Assert.assertNull(result.get("end"));
    Assert.assertNull(result.get("getOff"));
    Assert.assertNull(result.get("getOn"));
  }

  // Should never come up in live data.
  @Test
  public void journeyRoutingSameInputTest() throws IOException {
    String startStop = "Stretford";
    String endStop = "Stretford";

    Map<String, String> result = journeyRoutingService.findChangeStop(startStop, endStop);

    Assert.assertEquals("StretfordOutgoing", result.get("start"));
    Assert.assertEquals("StretfordOutgoing", result.get("end"));
    Assert.assertEquals("None", result.get("getOff"));
    Assert.assertEquals("None", result.get("getOn"));
  }
    }
