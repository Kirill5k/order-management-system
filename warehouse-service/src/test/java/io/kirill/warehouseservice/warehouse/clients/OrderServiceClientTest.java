package io.kirill.warehouseservice.warehouse.clients;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import io.kirill.warehouseservice.warehouse.clients.events.StockConfirmationEvent;
import io.kirill.warehouseservice.warehouse.clients.events.StockRejectionEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class OrderServiceClientTest {

  @Mock
  KafkaTemplate<String, Object> kafkaTemplate;

  @InjectMocks
  OrderServiceClient orderServiceClient;

  String orderId = "order-id";

  @Captor
  ArgumentCaptor<StockRejectionEvent> stockRejectionEventArgumentCaptor;

  @Captor
  ArgumentCaptor<StockConfirmationEvent> stockConfirmationEventArgumentCaptor;

  @Test
  void sendStockReservationFailure() {
    var error = "error-message";
    orderServiceClient.sendStockReservationFailure(orderId, error);

    verify(kafkaTemplate).send(eq("order.stock.reject"), eq("order-id-stock-rejection"),  stockRejectionEventArgumentCaptor.capture());

    var sentEvent = stockRejectionEventArgumentCaptor.getValue();
    assertThat(sentEvent.getOrderId()).isEqualTo(orderId);
    assertThat(sentEvent.getMessage()).isEqualTo(error);
  }

  @Test
  void sendStockReservationSuccess() {
    orderServiceClient.sendStockReservationSuccess(orderId);

    verify(kafkaTemplate).send(eq("order.stock.confirm"), eq("order-id-stock-confirmation"),  stockConfirmationEventArgumentCaptor.capture());

    var sentEvent = stockConfirmationEventArgumentCaptor.getValue();
    assertThat(sentEvent.getOrderId()).isEqualTo(orderId);
  }
}