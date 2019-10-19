package io.kirill.orderservice.order.listeners;

import io.kirill.orderservice.order.OrderService;
import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.domain.OrderBuilder;
import io.kirill.orderservice.order.listeners.events.StockConfirmationEvent;
import io.kirill.orderservice.order.listeners.events.StockRejectionEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static io.kirill.orderservice.order.domain.OrderStatus.CANCELLED_OUT_OF_STOCK;
import static io.kirill.orderservice.order.domain.OrderStatus.RESERVED_PROCESSING_PAYMENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockEventsListenerTest {

  @Mock
  OrderService orderService;

  @InjectMocks
  StockEventsListener stockEventsListener;

  String orderId = "order-1";
  Order order = OrderBuilder.get().id(orderId).build();

  @Test
  void confirmStock() {
    var stockConfirmationEvent = new StockConfirmationEvent(orderId);

    when(orderService.updateStatus(any(), any())).thenReturn(Mono.just(order));

    stockEventsListener.confirmStock(stockConfirmationEvent);

    verify(orderService, timeout(500)).updateStatus(orderId, RESERVED_PROCESSING_PAYMENT);
    verify(orderService, timeout(500)).processPayment(order);
  }

  @Test
  void rejectStock() {
    var stockRejectionEvent = new StockRejectionEvent(orderId, "error-message");

    when(orderService.updateStatus(any(), any())).thenReturn(Mono.just(order));

    stockEventsListener.rejectStock(stockRejectionEvent);

    verify(orderService, timeout(500)).updateStatus(orderId, CANCELLED_OUT_OF_STOCK);
  }
}