package io.kirill.financeservice.finance;

import io.kirill.financeservice.finance.domain.Address;
import io.kirill.financeservice.finance.domain.Order;
import io.kirill.financeservice.finance.domain.OrderLine;
import io.kirill.financeservice.finance.domain.PaymentDetails;
import java.time.Instant;
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
    var paymentDetails = PaymentDetails.builder()
        .nameOnCard("Joan Merkley")
        .cardType("Visa")
        .cardNumber("4038838805770816")
        .cvv(197)
        .expires("07/2023")
        .build();
    return Order.builder()
        .id("order-1")
        .dateCreated(Instant.now())
        .customerId("customer-1")
        .billingAddress(address)
        .paymentDetails(paymentDetails)
        .orderLines(List.of(new OrderLine("item-1", 3), new OrderLine("item-2", 3)));
  }
}
