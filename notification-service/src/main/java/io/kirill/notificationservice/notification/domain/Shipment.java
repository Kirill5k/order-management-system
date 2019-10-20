package io.kirill.notificationservice.notification.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.With;

import java.time.Instant;

@Value
@Builder
@With
@RequiredArgsConstructor
public class Shipment {
  private final String id;
  private final String orderId;
  private final String customerId;
  private final Address shippingAddress;
  private final Instant dateCreated;
  private final Instant dateShipped;
}
