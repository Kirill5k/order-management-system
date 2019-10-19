package io.kirill.orderservice.order.listeners;

import io.kirill.orderservice.order.OrderService;
import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.domain.OrderBuilder;
import io.kirill.orderservice.order.listeners.events.PaymentConfirmationEvent;
import io.kirill.orderservice.order.listeners.events.PaymentRejectionEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static io.kirill.orderservice.order.domain.OrderStatus.CANCELLED_PAYMENT_REJECTED;
import static io.kirill.orderservice.order.domain.OrderStatus.PAYED_PREPARING_FOR_SHIPMENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentEventsListenerTest {

  @Mock
  OrderService orderService;

  @InjectMocks
  PaymentEventsListener paymentEventsListener;

  String orderId = "order-1";
  Order order = OrderBuilder.get().id(orderId).build();

  @Test
  void confirmPayment() {
    var paymentConfirmationEvent = new PaymentConfirmationEvent(orderId);

    when(orderService.updateStatus(any(), any())).thenReturn(Mono.just(order));

    paymentEventsListener.confirmPayment(paymentConfirmationEvent);

    verify(orderService, timeout(500)).updateStatus(orderId, PAYED_PREPARING_FOR_SHIPMENT);
    verify(orderService, timeout(500)).dispatch(order);
  }

  @Test
  void rejectPayment() {
    var paymentRejectionEvent = new PaymentRejectionEvent(orderId, "error-message");

    when(orderService.updateStatus(any(), any())).thenReturn(Mono.just(order));

    paymentEventsListener.rejectPayment(paymentRejectionEvent);

    verify(orderService, timeout(500)).updateStatus(orderId, CANCELLED_PAYMENT_REJECTED);
    verify(orderService, timeout(500)).releaseStock(order);
  }
}