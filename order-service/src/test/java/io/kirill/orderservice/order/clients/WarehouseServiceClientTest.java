package io.kirill.orderservice.order.clients;

import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.domain.OrderBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceClientTest {

  @Mock
  KafkaTemplate<String, Object> kafkaTemplate;

  @InjectMocks
  WarehouseServiceClient warehouseServiceClient;

  @Captor
  ArgumentCaptor<Order> orderArgumentCaptor;

  String orderId = "order-1";
  Order order =  OrderBuilder.get().id(orderId).build();

  @Test
  void sendStockReservationEvent() {
    warehouseServiceClient.sendStockReservationEvent(order);

    verify(kafkaTemplate).send(eq("warehouse.stock.reserve"), eq("order-1-stock-reservation"),  orderArgumentCaptor.capture());

    var sentEvent = orderArgumentCaptor.getValue();
    assertThat(sentEvent).isEqualToComparingFieldByField(order);
  }

  @Test
  void sendStockReleaseEvent() {
    warehouseServiceClient.sendStockReleaseEvent(order);

    verify(kafkaTemplate).send(eq("warehouse.stock.release"), eq("order-1-stock-release"),  orderArgumentCaptor.capture());

    var sentEvent = orderArgumentCaptor.getValue();
    assertThat(sentEvent).isEqualToComparingFieldByField(order);
  }

  @Test
  void sendOrderDispatchEvent() {
    warehouseServiceClient.sendOrderDispatchEvent(order);

    verify(kafkaTemplate).send(eq("warehouse.shipment.dispatch"), eq("order-1-shipment-dispatch"),  orderArgumentCaptor.capture());

    var sentEvent = orderArgumentCaptor.getValue();
    assertThat(sentEvent).isEqualToComparingFieldByField(order);
  }
}