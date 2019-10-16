package io.kirill.orderservice.order.controllers;

import static org.springframework.http.HttpStatus.CREATED;

import io.kirill.orderservice.order.OrderService;
import io.kirill.orderservice.order.controllers.models.CreateOrderRequest;
import io.kirill.orderservice.order.controllers.models.CreateOrderResponse;
import io.kirill.orderservice.order.domain.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
  private final OrderService orderService;

  @ResponseStatus(CREATED)
  @PostMapping
  public Mono<CreateOrderResponse> initiate(@Validated @RequestBody CreateOrderRequest request) {
    log.info("initiate order request from customer {}", request.getCustomerId());
    return orderService.create(request.toOrder())
        .doOnNext(orderService::reserveStock)
        .map(Order::getId)
        .map(CreateOrderResponse::new);
  }
}
