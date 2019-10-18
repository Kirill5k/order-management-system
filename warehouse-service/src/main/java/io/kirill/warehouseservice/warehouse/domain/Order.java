package io.kirill.warehouseservice.warehouse.domain;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Order {
  private final String id;
  private final List<OrderLine> orderLines;
}
