package io.kirill.financeservice.finance.clients;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.kirill.financeservice.common.exceptions.ServiceApiException;
import io.kirill.financeservice.common.models.ApiErrorResponse;
import io.kirill.financeservice.finance.domain.ProductItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
public class CatalogueServiceClient {
  private final WebClient webClient;

  public CatalogueServiceClient(WebClient.Builder webClientBuilder, @Value("${app.catalogue-service.products-uri}") String baseUri) {
    this.webClient = webClientBuilder
        .baseUrl(baseUri)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
        .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
        .build();
  }

  public Mono<ProductItem> findProductItem(String itemId) {
    return webClient
        .get()
        .uri(builder -> builder.path(itemId).build())
        .retrieve()
        .onStatus(HttpStatus::isError, CatalogueServiceClient::mapErrorResponse)
        .bodyToMono(ProductItem.class);
  }

  private static Mono<Throwable> mapErrorResponse(ClientResponse response) {
    return response.bodyToMono(ApiErrorResponse.class)
        .flatMap(error -> Mono.error(new ServiceApiException(response.statusCode(), error.getMessage())));
  }
}
