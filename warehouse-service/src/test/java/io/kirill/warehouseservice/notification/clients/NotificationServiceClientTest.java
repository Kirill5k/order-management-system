package io.kirill.warehouseservice.notification.clients;

import io.kirill.warehouseservice.notification.clients.events.OrderCancellationEvent;
import io.kirill.warehouseservice.warehouse.domain.Shipment;
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
class NotificationServiceClientTest {
  @Mock
  KafkaTemplate<String, Object> kafkaTemplate;

  @InjectMocks
  NotificationServiceClient notificationServiceClient;

  @Captor
  ArgumentCaptor<OrderCancellationEvent> orderCancellationEventArgumentCaptor;

  String customerId = "customer-1";
  String orderId = "order-1";


  @Test
  void sendOrderCancellationEvent() {
    var error = "error-message";

    notificationServiceClient.sendOrderCancellationEvent(customerId, orderId, error);

    verify(kafkaTemplate).send(eq("notification.order.cancelled"), eq("order-1-notify-cancelled"),  orderCancellationEventArgumentCaptor.capture());

    var sentEvent = orderCancellationEventArgumentCaptor.getValue();
    assertThat(sentEvent.getOrderId()).isEqualTo(orderId);
    assertThat(sentEvent.getCustomerId()).isEqualTo(customerId);
    assertThat(sentEvent.getMessage()).isEqualTo(error);
  }

  @Test
  void sendShipmentPreparationEvent() {
    var shipment = Shipment.builder().orderId(orderId).build();

    notificationServiceClient.sendShipmentPreparationEvent(shipment);

    verify(kafkaTemplate).send("notification.order.shipping", "order-1-notify-shipment",  shipment);
  }
}