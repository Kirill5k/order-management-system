package io.kirill.catalogueservice.product;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import io.kirill.catalogueservice.product.exceptions.ItemNotFound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(ProductItemController.class)
class ProductItemControllerTest {

  @Autowired
  WebTestClient client;

  @MockBean
  ProductItemService productItemService;

  String itemId = "item-1";
  ProductItem item = ProductItemBuilder.get().id(itemId).build();

  @Test
  void getOne() {
    doAnswer(invocation -> Mono.just(item))
        .when(productItemService)
        .get(itemId);

    client
        .get()
        .uri("/products/item-1")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentTypeCompatibleWith(APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isEqualTo(itemId);

    verify(productItemService).get(itemId);
  }

  @Test
  void getOneWhenNotFound() {
    doAnswer(invocation -> Mono.error(new ItemNotFound(itemId)))
        .when(productItemService)
        .get(itemId);

    client
        .get()
        .uri("/products/item-1")
        .exchange()
        .expectStatus().isNotFound()
        .expectHeader().contentTypeCompatibleWith(APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.message").isEqualTo("item with id item-1 does not exist");

    verify(productItemService).get(itemId);
  }
}