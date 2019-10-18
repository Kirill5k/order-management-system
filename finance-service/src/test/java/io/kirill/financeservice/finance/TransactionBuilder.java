package io.kirill.financeservice.finance;

import io.kirill.financeservice.finance.domain.Address;
import io.kirill.financeservice.finance.domain.PaymentDetails;
import io.kirill.financeservice.finance.domain.Transaction;
import io.kirill.financeservice.finance.domain.TransactionLine;
import java.math.BigDecimal;
import java.util.List;

public class TransactionBuilder {

  public static Transaction.TransactionBuilder get() {
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
    return Transaction.builder()
        .orderId("order-1")
        .customerId("customer-1")
        .billingAddress(address)
        .paymentDetails(paymentDetails)
        .transactionLines(List.of(new TransactionLine("item-1", "item - description", 3, BigDecimal.ONE)));
  }
}
