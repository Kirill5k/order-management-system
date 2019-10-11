package io.kirill.orderservice.order;

import io.kirill.orderservice.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;

  public Mono<Order> create(Order order) {
    return orderRepository.save(order);
  }
}
