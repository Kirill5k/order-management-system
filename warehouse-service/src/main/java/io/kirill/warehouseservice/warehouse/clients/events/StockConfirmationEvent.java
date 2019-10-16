package io.kirill.warehouseservice.warehouse.clients.events;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class StockConfirmationEvent {
  private final String orderId;
}
