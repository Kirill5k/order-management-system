package io.kirill.orderservice.order.clients;

import static io.kirill.orderservice.common.configs.KafkaConfig.WAREHOUSE_STOCK_RESERVE_TOPIC;

import io.kirill.orderservice.order.clients.events.StockReservationEvent;
import io.kirill.orderservice.order.domain.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseServiceClient {
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void sendStockReservationEvent(Order order) {
    log.info("reserving stock for order {} by customer {}", order.getId(), order.getCustomerId());
    var event = new StockReservationEvent(order);
    var key = String.format("%s-stock-reservation", order.getId());
    kafkaTemplate.send(WAREHOUSE_STOCK_RESERVE_TOPIC, key, event);
  }
}
