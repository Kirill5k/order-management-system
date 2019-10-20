package io.kirill.notificationservice.notification;

import io.kirill.notificationservice.notification.clients.CustomerServiceClient;
import io.kirill.notificationservice.notification.domain.Invoice;
import io.kirill.notificationservice.notification.domain.Shipment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotificationService {
  private final CustomerServiceClient customerServiceClient;

  public Mono<Void> notifyAboutCancellation(String customerId, String orderId, String message) {
    return Mono.empty();
  }

  public Mono<Void> notifyAboutShipping(Shipment shipment) {
    return Mono.empty();
  }

  public Mono<Void> notifyAboutPayment(Invoice invoice) {
    return Mono.empty();
  }
}
