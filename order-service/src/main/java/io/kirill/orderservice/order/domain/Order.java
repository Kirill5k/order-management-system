package io.kirill.orderservice.order.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;


@Document
@Value
@Builder
@With
@RequiredArgsConstructor
public class Order {
  @Id
  private final String id;
  private final String customerId;
  private final OrderStatus status;
  private final List<OrderLine> orderLines;
  private final Address shippingAddress;
  private final Address billingAddress;
  private final PaymentDetails paymentDetails;
  private final Instant dateCreated;
  private final Instant dateUpdated;
}
