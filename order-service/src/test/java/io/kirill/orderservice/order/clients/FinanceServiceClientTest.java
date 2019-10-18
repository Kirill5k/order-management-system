package io.kirill.orderservice.order.clients;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

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

@ExtendWith(MockitoExtension.class)
class FinanceServiceClientTest {

  @Mock
  KafkaTemplate<String, Object> kafkaTemplate;

  @InjectMocks
  FinanceServiceClient financeServiceClient;

  @Captor
  ArgumentCaptor<Order> orderArgumentCaptor;

  @Test
  void sendPaymentProcessingEvent() {
    var order = OrderBuilder.get().id("id1").build();

    financeServiceClient.sendPaymentProcessingEvent(order);

    verify(kafkaTemplate).send(eq("finance.payment.process"), eq("id1-payment-processing"),  orderArgumentCaptor.capture());

    var sentEvent = orderArgumentCaptor.getValue();
    assertThat(sentEvent).isEqualToComparingFieldByField(order);
  }
}