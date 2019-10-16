package io.kirill.warehouseservice.warehouse;

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
    return stockLineRepository.existsById(itemId)
        .filter(exists -> exists)
        .switchIfEmpty(Mono.error(new ItemNotFound(itemId)))
        .flatMap(exists -> stockLineRepository.findById(itemId))
        .flatMap(sl -> sl.getAmountAvailable() >= amountRequired ? Mono.empty() : Mono.error(new ItemNotInStock(itemId, sl.getAmountAvailable(),amountRequired )));
  }
}
