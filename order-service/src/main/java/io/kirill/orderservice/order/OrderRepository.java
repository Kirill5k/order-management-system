package io.kirill.orderservice.order;

import io.kirill.orderservice.order.domain.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface OrderRepository extends ReactiveMongoRepository<Order, String> {
}
