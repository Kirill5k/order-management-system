package io.kirill.orderservice.order;

import io.kirill.orderservice.order.clients.WarehouseClient;
import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final WarehouseClient warehouseClient;

  public Mono<Order> create(Order order) {
    return orderRepository.save(order)
      .doOnNext(warehouseClient::verifyStock);
  }
}
