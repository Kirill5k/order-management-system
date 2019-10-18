package io.kirill.financeservice.finance.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Document
@Builder
@With
@RequiredArgsConstructor
public class Transaction {
  @Id
  private final String id;
  private final String orderId;
  private final String customerId;
  private final List<TransactionLine> transactionLines;
  private final PaymentDetails paymentDetails;
  private final Address billingAddress;
  private final BigDecimal totalChargeAmount;
  private final Instant dateCreated;
}
