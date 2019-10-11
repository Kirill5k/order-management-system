package io.kirill.orderservice.order;

import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.models.CreateOrderRequest;
import io.kirill.orderservice.order.models.CreateOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
  private final OrderService orderService;

  @ResponseStatus(CREATED)
  @PostMapping
  public Mono<CreateOrderResponse> create(@Validated @RequestBody CreateOrderRequest request) {
    return orderService.create(request.toOrder())
      .map(Order::getId)
      .map(CreateOrderResponse::new);
  }
}
