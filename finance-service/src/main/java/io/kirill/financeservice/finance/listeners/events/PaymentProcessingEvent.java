package io.kirill.financeservice.finance.listeners.events;

import io.kirill.financeservice.finance.domain.Address;
import io.kirill.financeservice.finance.domain.OrderLine;
import io.kirill.financeservice.finance.domain.PaymentDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class PaymentProcessingEvent {
  private final String orderId;
  private final String customerId;
  private final List<OrderLine> orderLines;
  private final PaymentDetails paymentDetails;
  private final Address billingAddress;
}
