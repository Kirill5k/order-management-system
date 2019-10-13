package io.kirill.orderservice.order.domain;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderBuilder {

  public static Order.OrderBuilder get() {
    var address = Address.builder()
      .line1("line1")
      .line2("line2")
      .city("city")
      .country("country")
      .county("county")
      .postcode("postcode")
      .build();
    return Order.builder()
      .id(UUID.randomUUID().toString())
      .customerId(UUID.randomUUID().toString())
      .orderLines(List.of(new OrderLine(UUID.randomUUID().toString(), 1)))
      .shippingAddress(address)
      .dateCreated(Instant.now())
      .billingAddress(address);
  }
}
