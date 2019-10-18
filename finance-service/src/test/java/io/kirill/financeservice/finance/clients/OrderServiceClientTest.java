package io.kirill.financeservice.finance.clients;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import io.kirill.financeservice.finance.TransactionBuilder;
import io.kirill.financeservice.finance.clients.events.PaymentConfirmationEvent;
import io.kirill.financeservice.finance.clients.events.PaymentRejectionEvent;
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

  @Captor
  ArgumentCaptor<PaymentRejectionEvent> paymentRejectionEventArgumentCaptor;

  @Captor
  ArgumentCaptor<PaymentConfirmationEvent> paymentConfirmationEventArgumentCaptor;

  String orderId = "order-1";

  @Test
  void sendPaymentProcessingFailure() {
    var error = "error-message";

    orderServiceClient.sendPaymentProcessingFailure(orderId, error);

    verify(kafkaTemplate).send(eq("order.payment.reject"), eq("order-1-payment-rejection"),  paymentRejectionEventArgumentCaptor.capture());

    var sentEvent = paymentRejectionEventArgumentCaptor.getValue();
    assertThat(sentEvent.getOrderId()).isEqualTo(orderId);
    assertThat(sentEvent.getMessage()).isEqualTo(error);
  }

  @Test
  void sendPaymentProcessingSuccess() {
    var transaction = TransactionBuilder.get().orderId(orderId).build();

    orderServiceClient.sendPaymentProcessingSuccess(transaction);

    verify(kafkaTemplate).send(eq("order.payment.confirm"), eq("order-1-payment-confirmation"),  paymentConfirmationEventArgumentCaptor.capture());

    var sentEvent = paymentConfirmationEventArgumentCaptor.getValue();
    assertThat(sentEvent.getOrderId()).isEqualTo(orderId);
  }
}