package io.kirill.orderservice.order.clients;

import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.clients.events.StockVerificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WarehouseClient {
  private final String WAREHOUSE_STOCK_TOPIC = "warehouse.stock";
  private final KafkaTemplate<String, StockVerificationEvent> kafkaTemplate;

  public void verifyStock(Order order) {
    var event = new StockVerificationEvent(order);
    var key = String.format("%s-stock-verification", order.getId());
    kafkaTemplate.send(WAREHOUSE_STOCK_TOPIC, key, event);
  }
}
