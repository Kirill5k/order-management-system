package io.kirill.financeservice.notification;

import io.kirill.financeservice.finance.domain.Invoice;
import io.kirill.financeservice.notification.clients.NotificationServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
  private final NotificationServiceClient notificationServiceClient;

  public void informCustomerAboutCancellation(String customerId, String orderId, String message) {
    notificationServiceClient.sendOrderCancellationEvent(customerId, orderId, message);
  }

  public void informCustomerAboutPayment(Invoice invoice) {
    notificationServiceClient.sendPaymentSuccessEvent(invoice);
  }
}
