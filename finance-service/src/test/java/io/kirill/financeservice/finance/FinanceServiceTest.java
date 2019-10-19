package io.kirill.financeservice.finance;

import io.kirill.financeservice.finance.clients.CatalogueServiceClient;
import io.kirill.financeservice.finance.clients.OrderServiceClient;
import io.kirill.financeservice.finance.domain.Order;
import io.kirill.financeservice.finance.domain.ProductItem;
import io.kirill.financeservice.finance.domain.Transaction;
import io.kirill.financeservice.finance.domain.TransactionLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinanceServiceTest {

  @Mock
  CatalogueServiceClient catalogueServiceClient;

  @Mock
  TransactionRepository transactionRepository;

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
    var orderDetails = OrderBuilder.get().build();

    doAnswer(inv -> Mono.just(item1))
        .when(catalogueServiceClient).findProductItem(itemId1);

    doAnswer(inv -> Mono.just(item2))
        .when(catalogueServiceClient).findProductItem(itemId2);

    doAnswer(inv -> Mono.just(((Transaction)inv.getArgument(0)).withId(transactionId)))
        .when(transactionRepository)
        .save(any());

    StepVerifier
        .create(financeService.processPayment(orderDetails))
        .expectNextMatches(transactionMatcher(orderDetails))
        .verifyComplete();

    verify(catalogueServiceClient).findProductItem(itemId1);
    verify(catalogueServiceClient).findProductItem(itemId2);
    verify(transactionRepository).save(any());
  }

  Predicate<Transaction> transactionMatcher(Order order) {
    return transaction -> transaction.getId().equals(transactionId)
        && transaction.getOrderId().equals(order.getId())
        && transaction.getCustomerId().equals(order.getCustomerId())
        && transaction.getDateCreated() != null
        && transaction.getTransactionLines().contains(new TransactionLine(item1, 3))
        && transaction.getTransactionLines().contains(new TransactionLine(item2, 3))
        && transaction.getPaymentDetails().equals(order.getPaymentDetails())
        && transaction.getBillingAddress().equals(order.getBillingAddress())
        && transaction.getTotalChargeAmount().equals(BigDecimal.valueOf(15.0));
  }

  @Test
  void processPaymentWhenError() {
    var orderDetails = OrderBuilder.get().build();

    doAnswer(inv -> Mono.just(item1))
        .when(catalogueServiceClient).findProductItem(itemId1);

    doAnswer(inv -> Mono.error(new RuntimeException("processing error")))
        .when(catalogueServiceClient).findProductItem(itemId2);

    StepVerifier
        .create(financeService.processPayment(orderDetails))
        .verifyErrorMatches(error -> error.getMessage().equals("processing error"));

    verify(catalogueServiceClient).findProductItem(itemId1);
    verify(catalogueServiceClient).findProductItem(itemId2);
    verify(transactionRepository, never()).save(any());
  }

  @Test
  void rejectPayment() {
    financeService.rejectPayment("order-1", "error");

    verify(orderServiceClient).sendPaymentProcessingFailure("order-1", "error");
  }

  @Test
  void confirmPayment() {
    var transaction = TransactionBuilder.get().orderId("order-1").build();

    financeService.confirmPayment(transaction);

    verify(orderServiceClient).sendPaymentProcessingSuccess("order-1");
  }
}