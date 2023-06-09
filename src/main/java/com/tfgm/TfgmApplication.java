package com.tfgm;

import com.tfgm.controllers.TramController;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import com.tfgm.services.TramStopService;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TfgmApplication {

  public static void main(String[] args)
      throws URISyntaxException, IOException, InterruptedException {
    //    SpringApplication.run(TfgmApplication.class, args);
    int count = 0;
    TramStopService tramService =
        new TramStopService("src/main/resources/static/TramStopData.json");

    while (true) {
      System.out.println("---------LOOOOP---------" + count);
      tramService.update();
      TimeUnit.SECONDS.sleep(10);
      count += 10;
    }
  }
}
