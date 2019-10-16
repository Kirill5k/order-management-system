package io.kirill.warehouseservice.warehouse.clients;

import static io.kirill.warehouseservice.common.configs.KafkaConfig.ORDER_STOCK_CONFIRM_TOPIC;
import static io.kirill.warehouseservice.common.configs.KafkaConfig.ORDER_STOCK_REJECT_TOPIC;

import io.kirill.warehouseservice.warehouse.clients.events.StockConfirmationEvent;
import io.kirill.warehouseservice.warehouse.clients.events.StockRejectionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderServiceClient {
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void sendStockReservationFailure(String orderId, String message) {
    log.info("rejecting stock reservation for order {}: {}", orderId, message);
    var event = new StockRejectionEvent(orderId, message);
    var key = String.format("%s-stock-rejection", orderId);
    kafkaTemplate.send(ORDER_STOCK_REJECT_TOPIC, key, event);
  }

  public void sendStockReservationSuccess(String orderId) {
    log.info("confirming stock reservation for order {}", orderId);
    var event = new StockConfirmationEvent(orderId);
    var key = String.format("%s-stock-confirmation", orderId);
    kafkaTemplate.send(ORDER_STOCK_CONFIRM_TOPIC, key, event);
  }
}
