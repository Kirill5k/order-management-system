package io.kirill.financeservice.finance.clients;

import io.kirill.financeservice.common.config.KafkaConfig;
import io.kirill.financeservice.finance.clients.events.PaymentConfirmationEvent;
import io.kirill.financeservice.finance.clients.events.PaymentRejectionEvent;
import io.kirill.financeservice.finance.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderServiceClient {
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void sendPaymentProcessingFailure(String orderId, String message) {
    log.info("rejecting payment for order {}: {}", orderId, message);
    var event = new PaymentRejectionEvent(orderId, message);
    var key = String.format("%s-payment-rejection", orderId);
    kafkaTemplate.send(KafkaConfig.ORDER_PAYMENT_REJECT_TOPIC, key, event);
  }

  public void sendPaymentProcessingSuccess(Transaction transaction) {
    log.info("confirming payment for order {}", transaction.getOrderId());
    var event = new PaymentConfirmationEvent(transaction);
    var key = String.format("%s-payment-confirmation", transaction.getOrderId());
    kafkaTemplate.send(KafkaConfig.ORDER_PAYMENT_CONFIRM_TOPIC, key, event);
  }
}
