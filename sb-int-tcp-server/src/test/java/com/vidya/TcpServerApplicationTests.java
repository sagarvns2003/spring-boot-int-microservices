package com.vidya;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootTest
class TcpServerApplicationTests {

  @Test
  void test_appMain() {
    String[] arguments = new String[] {"Hi", "Hello"};
    try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
      mocked
          .when(
              () -> {
                SpringApplication.run(TcpServerApplication.class, arguments);
              })
          .thenReturn(Mockito.mock(ConfigurableApplicationContext.class));
      TcpServerApplication.main(arguments);

      mocked.verify(
          () -> {
            SpringApplication.run(TcpServerApplication.class, new String[] {"Hi", "Hello"});
          });
    }
  }
}
