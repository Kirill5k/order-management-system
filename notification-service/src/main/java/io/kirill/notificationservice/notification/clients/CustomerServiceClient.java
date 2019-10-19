package io.kirill.notificationservice.notification.clients;

import io.kirill.notificationservice.common.exception.ServiceApiException;
import io.kirill.notificationservice.common.models.ApiErrorResponse;
import io.kirill.notificationservice.notification.domain.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class CustomerServiceClient {
  private final WebClient webClient;

  public CustomerServiceClient(WebClient.Builder webClientBuilder, @Value("${app.customer-service.customers-uri}") String baseUri) {
    this.webClient = webClientBuilder
        .baseUrl(baseUri)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
        .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
        .build();
  }

  public Mono<Customer> findCustomer(String itemId) {
    return webClient
        .get()
        .uri(builder -> builder.path(itemId).build())
        .retrieve()
        .onStatus(HttpStatus::isError, CustomerServiceClient::mapErrorResponse)
        .bodyToMono(Customer.class);
  }

  private static Mono<Throwable> mapErrorResponse(ClientResponse response) {
    return response.bodyToMono(ApiErrorResponse.class)
        .flatMap(error -> Mono.error(new ServiceApiException(response.statusCode(), error.getMessage())));
  }
}
