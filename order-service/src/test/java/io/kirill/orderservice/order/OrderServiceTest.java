package io.kirill.orderservice.order;

import static io.kirill.orderservice.order.domain.OrderStatus.RESERVED_PROCESSING_PAYMENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.kirill.orderservice.order.clients.FinanceServiceClient;
import io.kirill.orderservice.order.clients.WarehouseServiceClient;
import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.domain.OrderBuilder;
import io.kirill.orderservice.order.domain.OrderStatus;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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

  @Test
  void create() {
    doAnswer(invocation -> Mono.just(invocation.getArgument(0))).when(orderRepository).save(any());

    var newOrder = OrderBuilder.get().build();
    var savedOrder = orderService.create(newOrder);

    StepVerifier
        .create(savedOrder)
        .expectNextMatches(order -> order.getId().equals(newOrder.getId()) && order.getCustomerId().equals(newOrder.getCustomerId()))
        .verifyComplete();

    verify(orderRepository).save(newOrder);
  }

  @Test
  void createReturnsError() {
    when(orderRepository.save(any())).thenReturn(Mono.error(RuntimeException::new));

    var newOrder = OrderBuilder.get().build();
    var savedOrder = orderService.create(newOrder);

    StepVerifier
        .create(savedOrder)
        .verifyError();

    verify(orderRepository).save(newOrder);
  }

  @Test
  void reserveStock() {
    var newOrder = OrderBuilder.get().build();

    orderService.reserveStock(newOrder);

    verify(warehouseServiceClient).sendStockReservationEvent(newOrder);
  }

  @Test
  void updateStatus() {
    var order = OrderBuilder.get().id(orderId).status(OrderStatus.INITIATED_RESERVING_STOCK).build();

    when(orderRepository.findById(orderId)).thenReturn(Mono.just(order));

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
    var order = OrderBuilder.get().build();

    orderService.processPayment(order);

    verify(financeServiceClient).sendPaymentProcessingEvent(order);
  }
}