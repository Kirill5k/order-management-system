package io.kirill.orderservice.order.domain;

import java.time.Instant;
import java.util.List;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Value
@Builder
@With
@RequiredArgsConstructor
public class Order {
  @Id
  private final String id;
  private final String customerId;
  private final Address shippingAddress;
  private final Address billingAddress;
  private final List<LineItem> lineItems;
  private final Instant dateCreated;
}
