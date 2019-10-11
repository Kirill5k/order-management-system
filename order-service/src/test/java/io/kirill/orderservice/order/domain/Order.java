package io.kirill.orderservice.order.domain;

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Value
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
