package junit.com.svenruppert.flow.client;

import com.svenruppert.dependencies.core.logger.HasLogger;
import com.svenruppert.flow.client.ExternalApiService;
import com.svenruppert.restserver.SimpleRestServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExternalApiServiceTest
    implements HasLogger {

  SimpleRestServer simpleRestServer = new SimpleRestServer();

  @BeforeEach
  void setUp()
      throws IOException {
    simpleRestServer.init();
  }

  @AfterEach
  void tearDown() {
    simpleRestServer.shutdown();
  }

  @Test
  void test001() {
    var externalApiService = new ExternalApiService("http://localhost:9090");
    try {
      var data = externalApiService.fetchData();
      assertNotNull(data);
      logger().info("Data: {}", data);
    } catch (IOException | InterruptedException e) {
      logger().warn(e.getMessage());
    }
  }
}