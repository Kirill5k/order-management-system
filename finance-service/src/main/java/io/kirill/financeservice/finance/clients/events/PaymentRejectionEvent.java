package io.kirill.financeservice.finance.clients.events;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class PaymentRejectionEvent {
  private final String orderId;
  private final String message;
}
