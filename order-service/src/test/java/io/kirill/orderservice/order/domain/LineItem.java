package io.kirill.orderservice.order.domain;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class LineItem {
  private final String itemId;
  private final Integer amount;
}
