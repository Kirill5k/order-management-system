package io.kirill.orderservice.order;

import io.kirill.orderservice.order.clients.FinanceServiceClient;
import io.kirill.orderservice.order.clients.WarehouseServiceClient;
import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.domain.OrderBuilder;
import io.kirill.orderservice.order.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static io.kirill.orderservice.order.domain.OrderStatus.RESERVED_PROCESSING_PAYMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  OrderRepository orderRepository;

  @Mock
  WarehouseServiceClient warehouseServiceClient;

  @Mock
  FinanceServiceClient financeServiceClient;

  @InjectMocks
  OrderService orderService;

  @Captor
  ArgumentCaptor<Order> orderArgumentCaptor;

  String orderId = "order-1";
  Order order = OrderBuilder.get().id(orderId).build();

  @Test
  void create() {
    doAnswer(invocation -> Mono.just(invocation.getArgument(0))).when(orderRepository).save(any());

    var savedOrder = orderService.create(order);

    StepVerifier
        .create(savedOrder)
        .expectNextMatches(order -> order.getId().equals(order.getId()) && order.getCustomerId().equals(order.getCustomerId()))
        .verifyComplete();

    verify(orderRepository).save(order);
  }

  @Test
  void createReturnsError() {
    when(orderRepository.save(any())).thenReturn(Mono.error(RuntimeException::new));

    var savedOrder = orderService.create(order);

    StepVerifier
        .create(savedOrder)
        .verifyError();

    verify(orderRepository).save(order);
  }

  @Test
  void reserveStock() {
    orderService.reserveStock(order);

    verify(warehouseServiceClient).sendStockReservationEvent(order);
  }

  @Test
  void updateStatus() {
    when(orderRepository.findById(orderId)).thenReturn(Mono.just(order.withStatus(OrderStatus.INITIATED_RESERVING_STOCK)));

    StepVerifier
        .create(orderService.updateStatus(orderId, RESERVED_PROCESSING_PAYMENT))
        .expectNextMatches(o -> o.getId().equals(orderId) && o.getStatus() == RESERVED_PROCESSING_PAYMENT && o.getDateUpdated() != null)
        .verifyComplete();

    verify(orderRepository).findById(orderId);
    verify(orderRepository).save(orderArgumentCaptor.capture());
    var updatedOrder = orderArgumentCaptor.getValue();
    assertThat(updatedOrder.getId()).isEqualTo(orderId);
    assertThat(updatedOrder.getDateUpdated()).isBetween(Instant.now().minusSeconds(30), Instant.now().plusSeconds(30));
  }

  @Test
  void processPayment() {
    orderService.processPayment(order);

    verify(financeServiceClient).sendPaymentProcessingEvent(order);
  }

  @Test
  void releaseStock() {
    orderService.releaseStock(order);

    verify(warehouseServiceClient).sendStockReleaseEvent(order);
  }

  @Test
  void dispatch() {
    orderService.dispatch(order);

    verify(warehouseServiceClient).sendOrderDispatchEvent(order);
  }
}