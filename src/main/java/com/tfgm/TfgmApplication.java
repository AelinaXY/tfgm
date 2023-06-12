package com.tfgm;

import com.tfgm.persistence.TramStopRepo;
import com.tfgm.services.TramStopService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/** */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class TfgmApplication {

  public static void main(String[] args) {
    SpringApplication.run(TfgmApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public static void mainRunner() throws IOException, URISyntaxException, InterruptedException {
    int count = 0;
    TramStopService tramService = new TramStopService(new TramStopRepo());

    while (true) {
      System.out.println("---------LOOOOP---------" + count);
      tramService.update();
      TimeUnit.SECONDS.sleep(10);
      count += 10;
    }
  }
}
