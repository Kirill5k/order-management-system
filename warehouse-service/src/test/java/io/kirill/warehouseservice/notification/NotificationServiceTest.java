package io.kirill.warehouseservice.notification;

import io.kirill.warehouseservice.notification.clients.NotificationServiceClient;
import io.kirill.warehouseservice.warehouse.domain.Shipment;
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
  void informCustomerAboutShipment() {
    var shipment = Shipment.builder().build();

    notificationService.informCustomerAboutShipment(shipment);

    verify(notificationServiceClient).sendShipmentPreparationEvent(shipment);
  }
}