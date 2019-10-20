package io.kirill.financeservice.notification;

import io.kirill.financeservice.finance.domain.Invoice;
import io.kirill.financeservice.notification.clients.NotificationServiceClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
  @Mock
  NotificationServiceClient notificationServiceClient;

  @InjectMocks
  NotificationService notificationService;

  @Test
  void informCustomerAboutCancellation() {
    notificationService.informCustomerAboutCancellation("customer-1", "order-1", "error-message");

    verify(notificationServiceClient).sendOrderCancellationEvent("customer-1", "order-1", "error-message");
  }

  @Test
  void informCustomerAboutPayment() {
    var invoice = Invoice.builder().build();

    notificationService.informCustomerAboutPayment(invoice);

    verify(notificationServiceClient).sendPaymentSuccessEvent(invoice);
  }
}