package io.kirill.customerservice.customer.customer;

import java.math.BigDecimal;

public class ProductItemBuilder {

  public static ProductItem.ProductItemBuilder get() {
    return ProductItem.builder()
        .id("item-1")
        .name("test item product")
        .price(BigDecimal.valueOf(9.99));
  }
}
