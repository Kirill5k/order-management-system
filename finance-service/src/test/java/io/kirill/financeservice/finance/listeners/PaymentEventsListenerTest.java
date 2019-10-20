package io.kirill.financeservice.finance.listeners;

import io.kirill.financeservice.finance.FinanceService;
import io.kirill.financeservice.finance.InvoiceBuilder;
import io.kirill.financeservice.finance.OrderBuilder;
import io.kirill.financeservice.finance.domain.Order;
import io.kirill.financeservice.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentEventsListenerTest {

  @Mock
  FinanceService financeService;

  @Mock
  NotificationService notificationService;

  @InjectMocks
  PaymentEventsListener paymentEventsListener;

  @Captor
  ArgumentCaptor<Order> orderDetailsArgumentCaptor;

  Order order = OrderBuilder.get().build();

  @Test
  void processPayment() {
    var invoice = InvoiceBuilder.get().build();

    doAnswer(invocation -> Mono.just(invoice))
        .when(financeService)
        .processPayment(orderDetailsArgumentCaptor.capture());

    paymentEventsListener.processPayment(order);

    verify(financeService, timeout(500)).processPayment(any());
    verify(financeService, timeout(500)).confirmPayment(invoice);
    verify(notificationService, timeout(500)).informCustomerAboutPayment(invoice);
    verify(financeService, never()).rejectPayment(anyString(), anyString());
    verify(notificationService, never()).informCustomerAboutCancellation(anyString(), anyString(), anyString());

    assertThat(orderDetailsArgumentCaptor.getValue()).isEqualToComparingFieldByField(order);
  }

  @Test
  void processPaymentWhenError() {
    doAnswer(invocation -> Mono.error(new RuntimeException("error")))
        .when(financeService)
        .processPayment(any());

    paymentEventsListener.processPayment(order);

    verify(financeService, timeout(500)).processPayment(any());
    verify(financeService, timeout(500)).rejectPayment(order.getId(), "error");
    verify(notificationService, timeout(500)).informCustomerAboutCancellation(order.getCustomerId(), order.getId(), "error");
    verify(financeService, never()).confirmPayment(any());
    verify(notificationService, never()).informCustomerAboutPayment(any());
  }
}