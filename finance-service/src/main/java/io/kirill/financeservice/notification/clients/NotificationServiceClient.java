package io.kirill.financeservice.notification.clients;

import io.kirill.financeservice.finance.domain.Invoice;
import io.kirill.financeservice.notification.clients.events.OrderCancellationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static io.kirill.financeservice.common.config.KafkaConfig.NOTIFICATION_ORDER_CANCELLED_TOPIC;
import static io.kirill.financeservice.common.config.KafkaConfig.NOTIFICATION_ORDER_PAYED_TOPIC;

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

  public void sendPaymentSuccessEvent(Invoice invoice) {
    log.info("informing customer {} about successfull payment for order {}", invoice.getCustomerId(), invoice.getOrderId());
    var key = String.format("%s-notify-invoice", invoice.getOrderId());
    kafkaTemplate.send(NOTIFICATION_ORDER_PAYED_TOPIC, key, invoice);
  }
}
