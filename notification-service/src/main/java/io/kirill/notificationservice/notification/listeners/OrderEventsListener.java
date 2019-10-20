package io.kirill.notificationservice.notification.listeners;

import io.kirill.notificationservice.common.config.KafkaConfig;
import io.kirill.notificationservice.notification.NotificationService;
import io.kirill.notificationservice.notification.domain.Invoice;
import io.kirill.notificationservice.notification.domain.Shipment;
import io.kirill.notificationservice.notification.listeners.events.OrderCancellationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventsListener {
  private final NotificationService notificationService;

  @KafkaListener(topics = KafkaConfig.NOTIFICATION_ORDER_CANCELLED_TOPIC)
  public void notifyAboutCancellation(@Payload Object event) {
    var oce = (OrderCancellationEvent) event;
    log.info("received order cancelled notification for customer {}", oce.getCustomerId());
    notificationService.notifyAboutCancellation(oce.getCustomerId(), oce.getOrderId(), oce.getMessage())
        .subscribe();
  }

  @KafkaListener(topics = KafkaConfig.NOTIFICATION_ORDER_SHIPPING_TOPIC)
  public void notifyAboutShipping(@Payload Object event) {
    var shipment = (Shipment) event;
    log.info("received shipment notification for customer {}", shipment.getCustomerId());
    notificationService.notifyAboutShipping(shipment)
        .subscribe();
  }

  @KafkaListener(topics = KafkaConfig.NOTIFICATION_ORDER_PAYED_TOPIC)
  public void notifyAboutPayment(@Payload Object event) {
    var invoice = (Invoice) event;
    log.info("received invoice notification for customer {}", invoice.getCustomerId());
    notificationService.notifyAboutPayment(invoice)
        .subscribe();
  }
}
