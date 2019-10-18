package io.kirill.financeservice.finance.listeners;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import io.kirill.financeservice.finance.FinanceService;
import io.kirill.financeservice.finance.OrderBuilder;
import io.kirill.financeservice.finance.TransactionBuilder;
import io.kirill.financeservice.finance.domain.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class PaymentEventsListenerTest {

  @Mock
  FinanceService financeService;

  @InjectMocks
  PaymentEventsListener paymentEventsListener;

  @Captor
  ArgumentCaptor<Order> orderDetailsArgumentCaptor;

  Order order = OrderBuilder.get().build();

  @Test
  void processPayment() {
    doAnswer(invocation -> Mono.just(TransactionBuilder.get().build()))
        .when(financeService)
        .processPayment(orderDetailsArgumentCaptor.capture());

    paymentEventsListener.processPayment(order);

    verify(financeService, timeout(500)).processPayment(any());
    verify(financeService, timeout(500)).confirmPayment(any());
    verify(financeService, never()).rejectPayment(anyString(), anyString());

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
    verify(financeService, never()).confirmPayment(any());
  }
}