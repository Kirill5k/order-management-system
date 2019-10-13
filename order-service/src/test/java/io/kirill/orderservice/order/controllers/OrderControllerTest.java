package io.kirill.orderservice.order.controllers;

import io.kirill.orderservice.order.OrderService;
import io.kirill.orderservice.order.controllers.models.CreateOrderRequest;
import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.domain.OrderBuilder;
import io.kirill.orderservice.order.domain.OrderStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest(OrderController.class)
class OrderControllerTest {

  @Autowired
  private WebTestClient client;

  @MockBean
  private OrderService orderService;

  @Captor
  private ArgumentCaptor<Order> orderArgumentCaptor;

  private Order testOrder = OrderBuilder.get().build();

  @Test
  void create() {
    var orderId = UUID.randomUUID().toString();
    doAnswer(invocation -> Mono.just(((Order)invocation.getArgument(0)).withId(orderId)))
      .when(orderService)
      .create(orderArgumentCaptor.capture());

    var createOrderRequest = CreateOrderRequest.builder()
      .customerId(testOrder.getCustomerId())
      .billingAddress(testOrder.getBillingAddress())
      .shippingAddress(testOrder.getShippingAddress())
      .orderLines(testOrder.getOrderLines())
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

    var createdOrder = orderArgumentCaptor.getValue();
    assertThat(createdOrder.getStatus()).isEqualTo(OrderStatus.PROCESSING);
    assertThat(createdOrder.getDateCreated()).isBetween(Instant.now().minusSeconds(20), Instant.now().plusSeconds(20));
  }

  @Test
  void createWhenValidationError() {
    var orderId = UUID.randomUUID().toString();
    doAnswer(invocation -> Mono.just(((Order)invocation.getArgument(0)).withId(orderId))).when(orderService).create(any());

    var createOrderRequest = CreateOrderRequest.builder().build();

    client
      .post()
      .uri("/orders")
      .bodyValue(createOrderRequest)
      .exchange()
      .expectStatus().isBadRequest()
      .expectHeader().contentTypeCompatibleWith(APPLICATION_JSON)
      .expectBody()
      .jsonPath("$.message").value(Matchers.containsString("billingAddress: must not be null"))
      .jsonPath("$.message").value(Matchers.containsString("orderLines: must not be empty"))
      .jsonPath("$.message").value(Matchers.containsString("shippingAddress: must not be null"))
      .jsonPath("$.message").value(Matchers.containsString("customerId: must not be empty"));
  }
}