package io.kirill.financeservice.finance.domain;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class TransactionLine {
  private final String itemId;
  private final String fullItemDescription;
  private final Integer quantity;
  private final BigDecimal price;

  public TransactionLine(ProductItem item, int quantity) {
    this.itemId = item.getId();
    this.fullItemDescription = String.format("%s - %s", item.getName(), item.getDescription());
    this.quantity = quantity;
    this.price = item.getPrice();
  }
}
