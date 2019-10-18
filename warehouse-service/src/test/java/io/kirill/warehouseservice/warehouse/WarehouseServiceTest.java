package io.kirill.warehouseservice.warehouse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import io.kirill.warehouseservice.warehouse.clients.OrderServiceClient;
import io.kirill.warehouseservice.warehouse.domain.OrderLine;
import io.kirill.warehouseservice.warehouse.domain.StockLine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceTest {
  @Mock
  StockLineRepository stockLineRepository;

  @Mock
  OrderServiceClient orderServiceClient;

  @InjectMocks
  WarehouseService warehouseService;

  String itemId = "item-1";
  String orderId = "order-1";

  @Test
  void verifyIsInStock() {
    var orderLine = new OrderLine(itemId, 1);
    var stockLine = new StockLine(itemId, 3, 1);
    doAnswer(invocation -> Mono.just(stockLine)).when(stockLineRepository).findById(anyString());

    StepVerifier
        .create(warehouseService.verifyIsInStock(orderLine))
        .verifyComplete();

    verify(stockLineRepository).findById(itemId);
  }

  @Test
  void verifyIsInStockWhenItemNotFound() {
    var orderLine = new OrderLine(itemId, 1);

    doAnswer(invocation -> Mono.empty()).when(stockLineRepository).findById(anyString());

    StepVerifier
        .create(warehouseService.verifyIsInStock(orderLine))
        .verifyErrorMatches(error -> error.getMessage().equals("item with id item-1 does not exist"));

    verify(stockLineRepository).findById(itemId);
  }

  @Test
  void verifyIsInStockWhenItemNotInStock() {
    var orderLine = new OrderLine(itemId, 3);
    var stockLine = new StockLine(itemId, 1, 1);

    doAnswer(invocation -> Mono.just(stockLine)).when(stockLineRepository).findById(anyString());

    StepVerifier
        .create(warehouseService.verifyIsInStock(orderLine))
        .verifyErrorMatches(error -> error.getMessage().equals("item with id item-1 is not in stock. available - 1, required - 3"));

    verify(stockLineRepository).findById(itemId);
  }

  @Test
  void reserveStock() {
    var orderLine = new OrderLine(itemId, 2);

    var stockLine = new StockLine(itemId, 3, 1);

    doAnswer(invocation -> Mono.just(invocation.getArgument(0))).when(stockLineRepository).save(any());
    doAnswer(invocation -> Mono.just(stockLine)).when(stockLineRepository).findById(anyString());

    StepVerifier
        .create(warehouseService.reserveStock(orderLine))
        .expectNextMatches(sl -> sl.getItemId().equals(itemId) && sl.getAmountAvailable().equals(1) && sl.getAmountReserved().equals(3))
        .verifyComplete();

    verify(stockLineRepository).findById(itemId);
    verify(stockLineRepository).save(any());
  }

  @Test
  void rejectStockReservation() {
    var message = "error-message";
    warehouseService.rejectStockReservation(orderId, message);

    verify(orderServiceClient).sendStockReservationFailure(orderId, message);
  }

  @Test
  void confirmStockReservation() {
    warehouseService.confirmStockReservation(orderId);

    verify(orderServiceClient).sendStockReservationSuccess(orderId);
  }
}