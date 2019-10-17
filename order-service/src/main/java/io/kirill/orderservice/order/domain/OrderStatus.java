package io.kirill.orderservice.order.domain;

public enum OrderStatus {
  INITIATED,
  STOCK_RESERVED,

  CANCELLED_OUT_OF_STOCK
}
