package io.kirill.warehouseservice.warehouse;

import io.kirill.warehouseservice.warehouse.domain.Address;
import io.kirill.warehouseservice.warehouse.domain.Order;
import io.kirill.warehouseservice.warehouse.domain.OrderLine;

import java.util.List;

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
        .id("order-1")
        .customerId("customer-1")
        .orderLines(List.of(new OrderLine("item-1", 1)))
        .shippingAddress(address);
  }
}
