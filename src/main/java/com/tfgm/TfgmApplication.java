package com.tfgm;

import com.tfgm.services.TramStopService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/** */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableMongoRepositories
public class TfgmApplication {

  @Autowired TramStopService tramService;

  public static void main(String[] args) {
    SpringApplication.run(TfgmApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void mainRunner()
      throws IOException, URISyntaxException, InterruptedException {
    int count = 0;

    while (true) {
      System.out.println("---------LOOOOP---------" + count);
      tramService.update();
      TimeUnit.SECONDS.sleep(10);
      count += 10;
    }
  }
}
