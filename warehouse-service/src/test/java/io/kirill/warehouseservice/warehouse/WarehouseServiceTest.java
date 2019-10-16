package io.kirill.warehouseservice.warehouse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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

  @InjectMocks
  WarehouseService warehouseService;

  String itemId = "1";

  @Test
  void verifyIsInStock() {
    var stockLine = new StockLine(itemId, 3, 1);

    doAnswer(invocation -> Mono.just(true)).when(stockLineRepository).existsById(anyString());
    doAnswer(invocation -> Mono.just(stockLine)).when(stockLineRepository).findById(anyString());

    StepVerifier
        .create(warehouseService.verifyIsInStock(itemId, 1))
        .verifyComplete();

    verify(stockLineRepository).existsById(itemId);
    verify(stockLineRepository).findById(itemId);
  }

  @Test
  void verifyIsInStockWhenItemNotFound() {
    doAnswer(invocation -> Mono.just(false)).when(stockLineRepository).existsById(anyString());

    StepVerifier
        .create(warehouseService.verifyIsInStock(itemId, 1))
        .verifyErrorMatches(error -> error.getMessage().equals("item with 1 does not exist"));

    verify(stockLineRepository).existsById(itemId);
    verify(stockLineRepository, never()).findById(anyString());
  }

  @Test
  void verifyIsInStockWhenItemNotInStock() {
    var stockLine = new StockLine(itemId, 1, 1);

    doAnswer(invocation -> Mono.just(true)).when(stockLineRepository).existsById(anyString());
    doAnswer(invocation -> Mono.just(stockLine)).when(stockLineRepository).findById(anyString());

    StepVerifier
        .create(warehouseService.verifyIsInStock(itemId, 3))
        .verifyErrorMatches(error -> error.getMessage().equals("item 1 is not in stock. available - 1, required - 3"));

    verify(stockLineRepository).existsById(itemId);
    verify(stockLineRepository).findById(itemId);
  }
}