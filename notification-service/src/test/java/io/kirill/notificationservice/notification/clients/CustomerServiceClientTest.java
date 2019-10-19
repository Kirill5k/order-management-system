package io.kirill.notificationservice.notification.clients;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

class CustomerServiceClientTest {
  private static final String CUSTOMER_SERVICE_URI = "/customer/";

  final MockWebServer mockWebServer = new MockWebServer();

  CustomerServiceClient customerServiceClient;

  String customerId = "customer-1";

  @BeforeEach
  void setup() {
    customerServiceClient = new CustomerServiceClient(WebClient.builder(), mockWebServer.url(CUSTOMER_SERVICE_URI).toString());
  }

  @AfterEach
  void tearDown() throws Exception {
    mockWebServer.shutdown();
  }

  @Test
  void findProductItem() throws Exception {
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody("{\"id\": \"customer-1\", \"email\":  \"test@test.com\"}")
        .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE));

    var foundItem = customerServiceClient.findCustomer(customerId);

    StepVerifier
        .create(foundItem)
        .expectNextMatches(item -> item.getId().equals(customerId) && item.getEmail().equals("test@test.com"))
        .verifyComplete();

    var recordedRequest = mockWebServer.takeRequest();
    assertThat(recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(APPLICATION_JSON_VALUE);
    assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT)).isEqualTo(APPLICATION_JSON_VALUE);
    assertThat(recordedRequest.getPath()).isEqualTo("/customer/customer-1");
    assertThat(recordedRequest.getMethod()).isEqualTo("GET");
  }

  @Test
  void findProductItemWhenReturnsError() throws Exception {
    mockWebServer.enqueue(new MockResponse()
        .setResponseCode(400)
        .setBody("{\"message\": \"error-message\"}")
        .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE));

    var foundItem = customerServiceClient.findCustomer(customerId);

    StepVerifier
        .create(foundItem)
        .verifyErrorMatches(error -> error.getMessage().equals("error-message"));

    var recordedRequest = mockWebServer.takeRequest();
    assertThat(recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo(APPLICATION_JSON_VALUE);
    assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT)).isEqualTo(APPLICATION_JSON_VALUE);
    assertThat(recordedRequest.getPath()).isEqualTo("/customer/customer-1");
    assertThat(recordedRequest.getMethod()).isEqualTo("GET");
  }
}