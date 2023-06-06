package com.tfgm;

import com.tfgm.controllers.TramController;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;


public final class App {
  private App() {}

  public static void main(String[] args)
      throws URISyntaxException, IOException, InterruptedException {

    int count = 0;
    TramController tramController =
        new TramController("src/main/resources/com/tfgm/TramStopData.json");

    while (true) {
      System.out.println("---------LOOOOP---------" + count);
      tramController.update();
      TimeUnit.SECONDS.sleep(10);
      count += 10;
    }
  }
}
