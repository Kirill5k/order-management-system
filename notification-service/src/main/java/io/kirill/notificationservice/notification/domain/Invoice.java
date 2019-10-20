package io.kirill.notificationservice.notification.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Value
@Builder
@With
@RequiredArgsConstructor
public class Invoice {
  private final String id;
  private final String orderId;
  private final String customerId;
  private final List<InvoiceLine> invoiceLines;
  private final PaymentDetails paymentDetails;
  private final Address billingAddress;
  private final BigDecimal totalChargeAmount;
  private final Instant dateCreated;
}
