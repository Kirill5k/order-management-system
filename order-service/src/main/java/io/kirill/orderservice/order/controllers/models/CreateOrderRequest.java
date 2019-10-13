package io.kirill.orderservice.order.controllers.models;

import io.kirill.orderservice.order.domain.Address;
import io.kirill.orderservice.order.domain.OrderLine;
import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.domain.OrderStatus;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Value
@Builder
@RequiredArgsConstructor
public class CreateOrderRequest {
  @NotEmpty
  private final String customerId;

  @NotEmpty
  @Valid
  private final List<OrderLine> orderLines;

  @NotNull
  @Valid
  private final Address shippingAddress;

  @NotNull
  @Valid
  private final Address billingAddress;

  public Order toOrder() {
    return Order.builder()
      .billingAddress(billingAddress)
      .shippingAddress(shippingAddress)
      .orderLines(orderLines)
      .customerId(customerId)
      .dateCreated(Instant.now())
      .status(OrderStatus.PROCESSING)
      .build();
  }
}