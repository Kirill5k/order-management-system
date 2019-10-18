package io.kirill.financeservice.finance.domain;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class ProductItem {
  private final String id;
  private final BigDecimal price;
  private final String name;
  private final String description;
}
