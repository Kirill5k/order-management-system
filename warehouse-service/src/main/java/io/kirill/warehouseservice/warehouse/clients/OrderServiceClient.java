package io.kirill.warehouseservice.warehouse.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderServiceClient {
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void sendStockReservationFailure(String orderId, String message) {

  }

  public void sendStockReservationSuccess(String orderId) {

  }
}
