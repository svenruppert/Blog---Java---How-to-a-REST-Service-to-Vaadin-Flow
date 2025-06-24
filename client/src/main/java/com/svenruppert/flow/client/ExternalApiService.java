package com.svenruppert.flow.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svenruppert.restdemo.DataModel;

public final class ExternalApiService {

  protected static final String REST_ENDPOINT = "/data";
  private final HttpClient client;
  private final ObjectMapper mapper;
  private final URI endpoint;

  public ExternalApiService(String baseUrl) {
    this.client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();
    this.mapper = new ObjectMapper();
    this.endpoint = URI.create(baseUrl + REST_ENDPOINT);
  }

  public DataModel.DataObject fetchData() throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(endpoint)
        .header("Accept", "application/json")
        .GET()
        .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
      throw new IOException("Unerwarteter Statuscode: " + response.statusCode());
    }

    return mapper.readValue(response.body(), DataModel.DataObject.class);
  }
}