package io.kirill.orderservice.order.clients.events;

import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.domain.OrderLine;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@RequiredArgsConstructor
public class StockVerificationEvent {
  private final String oderId;
  private final List<OrderLine> orderLines;

  public StockVerificationEvent(Order order) {
    this.oderId = order.getId();
    this.orderLines = order.getOrderLines();
  }
}
