package io.kirill.orderservice.order.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import io.kirill.orderservice.order.OrderService;
import io.kirill.orderservice.order.controllers.models.CreateOrderRequest;
import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.domain.OrderBuilder;
import io.kirill.orderservice.order.domain.OrderStatus;
import java.time.Instant;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(OrderController.class)
class OrderControllerTest {

  @Autowired
  WebTestClient client;

  @MockBean
  OrderService orderService;

  @Captor
  ArgumentCaptor<Order> orderArgumentCaptor;

  Order testOrder = OrderBuilder.get().build();

  @Test
  void create() {
    var orderId = UUID.randomUUID().toString();
    doAnswer(invocation -> Mono.just(((Order) invocation.getArgument(0)).withId(orderId)))
        .when(orderService)
        .create(orderArgumentCaptor.capture());

    var createOrderRequest = CreateOrderRequest.builder()
        .customerId(testOrder.getCustomerId())
        .billingAddress(testOrder.getBillingAddress())
        .shippingAddress(testOrder.getShippingAddress())
        .orderLines(testOrder.getOrderLines())
        .paymentDetails(testOrder.getPaymentDetails())
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
    assertThat(createdOrder.getStatus()).isEqualTo(OrderStatus.INITIATED_RESERVING_STOCK);
    assertThat(createdOrder.getDateCreated()).isBetween(Instant.now().minusSeconds(20), Instant.now().plusSeconds(20));
    verify(orderService).reserveStock(createdOrder.withId(orderId));
  }

  @Test
  void createWhenValidationError() {
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
        .jsonPath("$.message").value(Matchers.containsString("paymentDetails: must not be null"))
        .jsonPath("$.message").value(Matchers.containsString("customerId: must not be empty"));

    verify(orderService, never()).create(any());
    verify(orderService, never()).reserveStock(any());
  }
}