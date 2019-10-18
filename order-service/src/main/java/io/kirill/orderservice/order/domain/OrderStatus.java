package io.kirill.orderservice.order.domain;

public enum OrderStatus {
  INITIATED_RESERVING_STOCK,
  RESERVED_PROCESSING_PAYMENT,

  CANCELLED_OUT_OF_STOCK
}
