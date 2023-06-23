package com.tfgm;

import com.tfgm.services.TramStopService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/** */
@SpringBootApplication()
@EntityScan("com.tfgm.models")
@EnableJpaRepositories("com.tfgm.persistence")
@EnableMongoRepositories
public class TfgmApplication {

  @Autowired TramStopService tramService;

  public static void main(String[] args) {
      SpringApplication.run(TfgmApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void mainRunner()
      throws IOException, URISyntaxException, InterruptedException {
    while (true) {
      System.out.println("---------LOOOOP---------" + Instant.now());
      tramService.update();
      TimeUnit.SECONDS.sleep(10);
    }
  }
}
