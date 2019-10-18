package io.kirill.financeservice.finance.clients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.math.BigDecimal;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CatalogueServiceClientTest {
  private static final String CATALOGUE_SERVICE_URI = "/products/";

  final MockWebServer mockWebServer = new MockWebServer();

  CatalogueServiceClient catalogueServiceClient;

  String itemId = "item-1";

  @BeforeEach
  void setup() {
    catalogueServiceClient = new CatalogueServiceClient(WebClient.builder(), mockWebServer.url(CATALOGUE_SERVICE_URI).toString());
  }

  @AfterEach
  void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  void findProductItem() throws Exception {
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody("{\"id\": \"item-1\", \"price\": 2.5}")
        .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE));

    var foundItem = catalogueServiceClient.findProductItem(itemId);

    StepVerifier
        .create(foundItem)
        .expectNextMatches(item -> item.getId().equals(itemId) && item.getPrice().equals(BigDecimal.valueOf(2.5)))
        .verifyComplete();

    var recordedRequest = mockWebServer.takeRequest();
    assertThat(recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(APPLICATION_JSON_VALUE);
    assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT)).isEqualTo(APPLICATION_JSON_VALUE);
    assertThat(recordedRequest.getPath()).isEqualTo("/products/item-1");
    assertThat(recordedRequest.getMethod()).isEqualTo("GET");
  }

  @Test
  void findProductItemWhenReturnsError() throws Exception {
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(400)
        .setBody("{\"message\": \"error-message\"}")
        .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE));

    var foundItem = catalogueServiceClient.findProductItem(itemId);

    StepVerifier
        .create(foundItem)
        .verifyErrorMatches(error -> error.getMessage().equals("error-message"));

    var recordedRequest = mockWebServer.takeRequest();
    assertThat(recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(APPLICATION_JSON_VALUE);
    assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT)).isEqualTo(APPLICATION_JSON_VALUE);
    assertThat(recordedRequest.getPath()).isEqualTo("/products/item-1");
    assertThat(recordedRequest.getMethod()).isEqualTo("GET");
  }
}