package io.kirill.warehouseservice.warehouse.listeners.events;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class StockReservationEvent {
  private final String orderId;
  private final List<OrderLine> orderLines;
}
