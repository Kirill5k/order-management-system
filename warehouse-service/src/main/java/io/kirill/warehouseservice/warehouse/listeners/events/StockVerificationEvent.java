package io.kirill.warehouseservice.warehouse.listeners.events;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class StockVerificationEvent {
  private final String oderId;
  private final List<OrderLine> orderLines;
}
