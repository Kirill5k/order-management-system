package io.kirill.warehouseservice.warehouse;

import io.kirill.warehouseservice.warehouse.domain.StockLine;
import io.kirill.warehouseservice.warehouse.exceptions.ItemNotFound;
import io.kirill.warehouseservice.warehouse.exceptions.ItemNotInStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WarehouseService {
  private final StockLineRepository stockLineRepository;

  public Mono<Void> verifyIsInStock(String itemId, int amountRequired) {
    return stockLineRepository.findById(itemId)
        .switchIfEmpty(Mono.error(new ItemNotFound(itemId)))
        .flatMap(sl -> sl.getAmountAvailable() >= amountRequired ? Mono.empty() : Mono.error(new ItemNotInStock(itemId, sl.getAmountAvailable(),amountRequired )));
  }

  public Mono<StockLine> reserveStock(String itemId, int amountRequired) {
    return stockLineRepository.findById(itemId)
        .map(sl -> sl.reserve(amountRequired))
        .flatMap(stockLineRepository::save);
  }
}
