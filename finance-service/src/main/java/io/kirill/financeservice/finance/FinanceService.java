package io.kirill.financeservice.finance;

import io.kirill.financeservice.finance.clients.CatalogueServiceClient;
import io.kirill.financeservice.finance.clients.OrderServiceClient;
import io.kirill.financeservice.finance.domain.Order;
import io.kirill.financeservice.finance.domain.OrderLine;
import io.kirill.financeservice.finance.domain.Transaction;
import io.kirill.financeservice.finance.domain.TransactionLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FinanceService {
  private final CatalogueServiceClient catalogueServiceClient;
  private final TransactionRepository transactionRepository;
  private final OrderServiceClient orderServiceClient;

  public Mono<Transaction> processPayment(Order order) {
    /* Heavy payment processing logic */
    return Flux.fromIterable(order.getOrderLines())
        .flatMap(this::createTransactionLine)
        .collectList()
        .map(order::toTransaction)
        .flatMap(transactionRepository::save);
  }

  private Mono<TransactionLine> createTransactionLine(OrderLine orderLine) {
    return catalogueServiceClient.findProductItem(orderLine.getItemId())
        .map(item -> new TransactionLine(item, orderLine.getQuantity()));
  }

  public void rejectPayment(String orderId, String message) {
    orderServiceClient.sendPaymentProcessingFailure(orderId, message);
  }

  public void confirmPayment(Transaction transaction) {
    orderServiceClient.sendPaymentProcessingSuccess(transaction);
  }
}
