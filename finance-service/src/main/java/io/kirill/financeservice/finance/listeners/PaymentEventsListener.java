package io.kirill.financeservice.finance.listeners;

import static io.kirill.financeservice.common.config.KafkaConfig.FINANCE_PAYMENT_PROCESS_TOPIC;

import io.kirill.financeservice.finance.FinanceService;
import io.kirill.financeservice.finance.listeners.events.PaymentProcessingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventsListener {
  private final FinanceService financeService;

  @KafkaListener(topics = FINANCE_PAYMENT_PROCESS_TOPIC)
  public void processPayment(@Payload Object paymentProcessingEvent) {
    var event = (PaymentProcessingEvent) paymentProcessingEvent;
    log.info("received payment processing event for order {}", event.getOrderId());
  }
}
