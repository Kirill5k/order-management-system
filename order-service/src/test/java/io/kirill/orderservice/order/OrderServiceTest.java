package io.kirill.orderservice.order;

import io.kirill.orderservice.order.clients.WarehouseClient;
import io.kirill.orderservice.order.domain.OrderBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  OrderRepository orderRepository;

  @Mock
  WarehouseClient warehouseClient;

  @InjectMocks
  OrderService orderService;

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
    verify(warehouseClient).reserveStock(newOrder);
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
    verify(warehouseClient, never()).reserveStock(any());
  }
}