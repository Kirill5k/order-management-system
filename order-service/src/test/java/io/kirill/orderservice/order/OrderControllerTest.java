package io.kirill.orderservice.order;

import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.domain.OrderBuilder;
import io.kirill.orderservice.order.models.CreateOrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest(OrderController.class)
class OrderControllerTest {

  @Autowired
  private WebTestClient client;

  @MockBean
  private OrderService orderService;

  private Order testOrder = OrderBuilder.get().build();

  @Test
  void create() {
    var orderId = UUID.randomUUID().toString();
    doAnswer(invocation -> Mono.just(((Order)invocation.getArgument(0)).withId(orderId))).when(orderService).create(any());

    var createOrderRequest = CreateOrderRequest.builder()
      .customerId(testOrder.getCustomerId())
      .billingAddress(testOrder.getBillingAddress())
      .shippingAddress(testOrder.getShippingAddress())
      .lineItems(testOrder.getLineItems())
      .build();

    client
      .post()
      .uri("/orders")
      .bodyValue(createOrderRequest)
      .exchange()
      .expectStatus().isCreated()
      .expectHeader().contentTypeCompatibleWith(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.id").isEqualTo(orderId);
  }
}