package io.kirill.financeservice.finance.clients.events;

import io.kirill.financeservice.finance.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class PaymentConfirmationEvent {
  private final String orderId;

  public PaymentConfirmationEvent(Transaction transaction) {
    orderId = transaction.getOrderId();
  }
}
