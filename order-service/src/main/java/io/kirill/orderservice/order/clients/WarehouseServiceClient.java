package io.kirill.orderservice.order.clients;

import io.kirill.orderservice.order.domain.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static io.kirill.orderservice.common.configs.KafkaConfig.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseServiceClient {
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void sendStockReservationEvent(Order order) {
    log.info("reserving stock for order {} by customer {}", order.getId(), order.getCustomerId());
    var key = String.format("%s-stock-reservation", order.getId());
    kafkaTemplate.send(WAREHOUSE_STOCK_RESERVE_TOPIC, key, order);
  }

  public void sendStockReleaseEvent(Order order) {
    log.info("releasing stock for order {}", order.getId());
    var key = String.format("%s-stock-release", order.getId());
    kafkaTemplate.send(WAREHOUSE_STOCK_RELEASE_TOPIC, key, order);
  }

  public void sendOrderDispatchEvent(Order order) {
    log.info("preparing order {} for dispatch", order.getId());
    var key = String.format("%s-shipment-dispatch", order.getId());
    kafkaTemplate.send(WAREHOUSE_SHIPMENT_DISPATCH_TOPIC, key, order);
  }
}
