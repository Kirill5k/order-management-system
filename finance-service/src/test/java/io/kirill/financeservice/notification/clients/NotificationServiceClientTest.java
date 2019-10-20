package io.kirill.financeservice.notification.clients;

import io.kirill.financeservice.finance.InvoiceBuilder;
import io.kirill.financeservice.notification.clients.events.OrderCancellationEvent;
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
  void sendPaymentSuccessEvent() {
    var invoice = InvoiceBuilder.get().build();

    notificationServiceClient.sendPaymentSuccessEvent(invoice);

    verify(kafkaTemplate).send("notification.order.paid", "order-1-notify-invoice",  invoice);
  }
}