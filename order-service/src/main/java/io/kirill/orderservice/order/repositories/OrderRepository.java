package io.kirill.orderservice.order.repositories;

import io.kirill.orderservice.order.domain.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
}
