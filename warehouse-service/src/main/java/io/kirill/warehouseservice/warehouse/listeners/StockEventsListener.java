package io.kirill.warehouseservice.warehouse.listeners;

import static io.kirill.warehouseservice.common.configs.KafkaConfig.WAREHOUSE_STOCK_RESERVE_TOPIC;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockEventsListener {

  @KafkaListener(topics = WAREHOUSE_STOCK_RESERVE_TOPIC)
  public void verifyStock(@Payload Object stockVerificationEvent) {
    log.info("received stock reservation event for order {}", stockVerificationEvent.toString());
  }
}
