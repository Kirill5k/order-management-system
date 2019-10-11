package io.kirill.orderservice.order.models;

import lombok.Value;
import org.springframework.web.bind.annotation.RequestMapping;

@Value
@RequestMapping
public class CreateOrderResponse {
  private final String id;
}
