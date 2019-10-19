package io.kirill.warehouseservice.warehouse.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Value
@Builder
@With
@RequiredArgsConstructor
public class Shipment {
  @Id
  private final String id;
  private final String orderId;
  private final String customerId;
  private final Address shippingAddress;
  private final Instant dateCreated;
  private final Instant dateShipped;
}
