package io.kirill.financeservice.finance.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class Order {
  private final String id;
  private final String customerId;
  private final List<OrderLine> orderLines;
  private final Address shippingAddress;
  private final Address billingAddress;
  private final PaymentDetails paymentDetails;
  private final Instant dateCreated;
  private final Instant dateUpdated;

  public Transaction toTransaction(List<TransactionLine> transactionLines) {
    var totalChargeAmount = transactionLines.stream()
        .map(tl -> tl.getPrice().multiply(BigDecimal.valueOf(tl.getQuantity())))
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    return Transaction.builder()
        .orderId(id)
        .customerId(customerId)
        .paymentDetails(paymentDetails)
        .billingAddress(billingAddress)
        .transactionLines(transactionLines)
        .dateCreated(Instant.now())
        .totalChargeAmount(totalChargeAmount)
        .build();
  }
}
