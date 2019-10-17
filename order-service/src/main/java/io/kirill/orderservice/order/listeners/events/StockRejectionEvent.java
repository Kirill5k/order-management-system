package io.kirill.orderservice.order.listeners.events;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class StockRejectionEvent {
  private final String orderId;
  private final String message;
}
