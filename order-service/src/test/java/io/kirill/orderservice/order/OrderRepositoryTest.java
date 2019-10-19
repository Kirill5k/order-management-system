package io.kirill.orderservice.order;

import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.domain.OrderBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;

@DataMongoTest
class OrderRepositoryTest {

  @Autowired
  private ReactiveMongoTemplate template;

  @Autowired
  private OrderRepository orderRepository;

  @Test
  void save() {
    var newOrder = OrderBuilder.get().id(null).build();
    var savedOrder = orderRepository.save(newOrder)
      .map(Order::getId)
      .flatMap(id -> template.findById(id, Order.class));

    StepVerifier
        .create(savedOrder)
        .expectNextMatches(order -> order.getId() != null && order.getCustomerId().equals("customer-1"))
        .verifyComplete();
  }

  @Test
  void findById() {
    var newOrder = OrderBuilder.get().build();

    var foundOrder = template.save(newOrder)
        .map(Order::getId)
        .flatMap(orderRepository::findById);

    StepVerifier
        .create(foundOrder)
        .expectNextMatches(order -> order.getId().equals(newOrder.getId()) && order.getCustomerId().equals(newOrder.getCustomerId()))
        .verifyComplete();
  }
}