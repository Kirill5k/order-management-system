package io.kirill.orderservice.order.domain;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class OrderLine {
  @NotEmpty
  private final String itemId;
  @Min(1)
  private final Integer quantity;
}
