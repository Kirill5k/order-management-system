package io.kirill.orderservice.order.clients;

import io.kirill.orderservice.order.clients.events.StockVerificationEvent;
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
class WarehouseClientTest {

  @Mock
  KafkaTemplate<String, StockVerificationEvent> kafkaTemplate;

  @InjectMocks
  WarehouseClient warehouseClient;

  @Captor
  ArgumentCaptor<StockVerificationEvent> stockVerificationEventArgumentCaptor;

  @Test
  void verifyStock() {
    var order = OrderBuilder.get().id("id1").build();

    warehouseClient.verifyStock(order);

    verify(kafkaTemplate).send(eq("warehouse.stock"), eq("id1-stock-verification"),  stockVerificationEventArgumentCaptor.capture());

    var sentEvent = stockVerificationEventArgumentCaptor.getValue();
    assertThat(sentEvent.getOderId()).isEqualTo(order.getId());
    assertThat(sentEvent.getOrderLines()).isEqualTo(order.getOrderLines());
  }
}