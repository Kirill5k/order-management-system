package io.kirill.orderservice.order.clients.events;

import io.kirill.orderservice.order.domain.Address;
import io.kirill.orderservice.order.domain.Order;
import io.kirill.orderservice.order.domain.OrderLine;
import io.kirill.orderservice.order.domain.PaymentDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class PaymentProcessingEvent {
  private final String orderId;
  private final String customerId;
  private final List<OrderLine> orderLines;
  private final PaymentDetails paymentDetails;
  private final Address billingAddress;

  public PaymentProcessingEvent(Order order) {
    orderId = order.getId();
    customerId = order.getCustomerId();
    orderLines = order.getOrderLines();
    paymentDetails = order.getPaymentDetails();
    billingAddress = order.getBillingAddress();
  }
}
