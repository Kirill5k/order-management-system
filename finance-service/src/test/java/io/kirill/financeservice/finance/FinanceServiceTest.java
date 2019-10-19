package io.kirill.financeservice.finance;

import io.kirill.financeservice.finance.clients.CatalogueServiceClient;
import io.kirill.financeservice.finance.clients.OrderServiceClient;
import io.kirill.financeservice.finance.domain.Order;
import io.kirill.financeservice.finance.domain.ProductItem;
import io.kirill.financeservice.finance.domain.Invoice;
import io.kirill.financeservice.finance.domain.InvoiceLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Random;
import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {

  @Mock
  CatalogueServiceClient catalogueServiceClient;

  @Mock
  InvoiceRepository invoiceRepository;

  @Mock
  OrderServiceClient orderServiceClient;

  @InjectMocks
  FinanceService financeService;

  String transactionId = "transaction-1";
  String itemId1 = "item-1";
  String itemId2 = "item-2";
  ProductItem item1 = new ProductItem(itemId1, BigDecimal.valueOf(2.5), "item1", "description");
  ProductItem item2 = new ProductItem(itemId2, BigDecimal.valueOf(2.5), "item2", "description");

  @Test
  void processPayment() {
    var rng = mock(Random.class);
    when(rng.nextBoolean()).thenReturn(true);
    var orderDetails = OrderBuilder.get().build();

    doAnswer(inv -> Mono.just(item1))
        .when(catalogueServiceClient).findProductItem(itemId1);

    doAnswer(inv -> Mono.just(item2))
        .when(catalogueServiceClient).findProductItem(itemId2);

    doAnswer(inv -> Mono.just(((Invoice)inv.getArgument(0)).withId(transactionId)))
        .when(invoiceRepository)
        .save(any());

    StepVerifier
        .create(financeService.processPayment(orderDetails, rng))
        .expectNextMatches(transactionMatcher(orderDetails))
        .verifyComplete();

    verify(catalogueServiceClient).findProductItem(itemId1);
    verify(catalogueServiceClient).findProductItem(itemId2);
    verify(invoiceRepository).save(any());
  }

  @Test
  void processPaymentWhenError() {
    var rng = mock(Random.class);
    when(rng.nextBoolean()).thenReturn(false);
    var orderDetails = OrderBuilder.get().build();

    doAnswer(inv -> Mono.just(item1))
        .when(catalogueServiceClient).findProductItem(itemId1);

    doAnswer(inv -> Mono.just(item2))
        .when(catalogueServiceClient).findProductItem(itemId2);

    StepVerifier
        .create(financeService.processPayment(orderDetails, rng))
        .verifyErrorMatches(error -> error.getMessage().equals("error processing payment for order order-1"));

    verify(catalogueServiceClient).findProductItem(itemId1);
    verify(catalogueServiceClient).findProductItem(itemId2);
    verify(invoiceRepository, never()).save(any());
  }

  Predicate<Invoice> transactionMatcher(Order order) {
    return transaction -> transaction.getId().equals(transactionId)
        && transaction.getOrderId().equals(order.getId())
        && transaction.getCustomerId().equals(order.getCustomerId())
        && transaction.getDateCreated() != null
        && transaction.getInvoiceLines().contains(new InvoiceLine(item1, 3))
        && transaction.getInvoiceLines().contains(new InvoiceLine(item2, 3))
        && transaction.getPaymentDetails().equals(order.getPaymentDetails())
        && transaction.getBillingAddress().equals(order.getBillingAddress())
        && transaction.getTotalChargeAmount().equals(BigDecimal.valueOf(15.0));
  }

  @Test
  void rejectPayment() {
    financeService.rejectPayment("order-1", "error");

    verify(orderServiceClient).sendPaymentProcessingFailure("order-1", "error");
  }

  @Test
  void confirmPayment() {
    var transaction = InvoiceBuilder.get().orderId("order-1").build();

    financeService.confirmPayment(transaction);

    verify(orderServiceClient).sendPaymentProcessingSuccess("order-1");
  }
}