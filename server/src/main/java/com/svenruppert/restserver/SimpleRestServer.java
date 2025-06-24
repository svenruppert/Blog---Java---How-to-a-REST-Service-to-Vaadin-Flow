package com.svenruppert.restserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.svenruppert.dependencies.core.logger.HasLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SimpleRestServer
    implements HasLogger {

  public static final int PORT = 9090;
  private HttpServer server;

  public static void main(String[] args)
      throws IOException {
    new SimpleRestServer().init();
  }

  public void init()
      throws IOException {
    server = HttpServer.create(new InetSocketAddress(PORT), 0);
    server.createContext("/data", new DataHandler());
    server.setExecutor(null); // default executor
    server.start();
    logger().info("Server läuft auf http://localhost:"+PORT+"/data");
  }

  public void shutdown() {
    server.stop(0);
    logger().info("Server gestoppt");
  }

  static class DataHandler
      implements HttpHandler, HasLogger  {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpExchange exchange)
        throws IOException {
      if (!"GET".equals(exchange.getRequestMethod())) {
        logger().info("Unerwartete HTTP-Methode: " + exchange.getRequestMethod() + " -");
        exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        return;
      }

      logger().info("GET-Request: " + exchange.getRequestURI());

      var data = new DataObject("abc123", "42.0");
      String response = mapper.writeValueAsString(data);

      exchange.getResponseHeaders().add("Content-Type", "application/json");
      exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
      try (OutputStream os = exchange.getResponseBody()) {
        os.write(response.getBytes(StandardCharsets.UTF_8));
      }
    }
  }

  public record DataObject(String id, String value) { }
}