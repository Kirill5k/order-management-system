package io.kirill.warehouseservice.notification.clients;

import io.kirill.warehouseservice.notification.clients.events.OrderCancellationEvent;
import io.kirill.warehouseservice.warehouse.domain.Shipment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static io.kirill.warehouseservice.common.configs.KafkaConfig.NOTIFICATION_ORDER_CANCELLED_TOPIC;
import static io.kirill.warehouseservice.common.configs.KafkaConfig.NOTIFICATION_ORDER_SHIPPING_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationServiceClient {
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void sendOrderCancellationEvent(String customerId, String orderId, String message) {
    log.info("informing customer {} about cancellation of his order {}", customerId, orderId);
    var event = new OrderCancellationEvent(orderId, customerId, message);
    var key = String.format("%s-notify-cancelled", orderId);
    kafkaTemplate.send(NOTIFICATION_ORDER_CANCELLED_TOPIC, key, event);
  }

  public void sendShipmentPreparationEvent(Shipment shipment) {
    log.info("informing customer {} about preparation of his order {} for shipment", shipment.getCustomerId(), shipment.getOrderId());
    var key = String.format("%s-notify-shipment", shipment.getOrderId());
    kafkaTemplate.send(NOTIFICATION_ORDER_SHIPPING_TOPIC, key, shipment);
  }
}
