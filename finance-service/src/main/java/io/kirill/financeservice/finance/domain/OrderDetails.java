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
public class OrderDetails {
  private final String orderId;
  private final String customerId;
  private final List<OrderLine> orderLines;
  private final PaymentDetails paymentDetails;
  private final Address billingAddress;

  public Transaction toTransaction(List<TransactionLine> transactionLines) {
    var totalChargeAmount = transactionLines.stream()
        .map(tl -> tl.getPrice().multiply(BigDecimal.valueOf(tl.getQuantity())))
        .reduce(BigDecimal::add)
        .orElseThrow(() -> new IllegalArgumentException("error during price calculation"));
    return Transaction.builder()
        .orderId(orderId)
        .customerId(customerId)
        .paymentDetails(paymentDetails)
        .billingAddress(billingAddress)
        .transactionLines(transactionLines)
        .dateCreated(Instant.now())
        .totalChargeAmount(totalChargeAmount)
        .build();
  }
}
