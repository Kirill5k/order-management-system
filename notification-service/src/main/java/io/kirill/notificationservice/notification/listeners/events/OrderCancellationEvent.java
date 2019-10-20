package io.kirill.notificationservice.notification.listeners.events;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class OrderCancellationEvent {
  private final String orderId;
  private final String customerId;
  private final String message;
}
