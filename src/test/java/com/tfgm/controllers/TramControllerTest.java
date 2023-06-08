package com.tfgm.controllers;

import java.io.IOException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TramControllerTest {

  @Mock
  TramController controller =
      new TramController("src/test/resources/com/tfgm/controllers/ExampleTramStopData.json");

  public TramControllerTest() throws IOException {}
}
