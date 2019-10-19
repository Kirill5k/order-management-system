package io.kirill.warehouseservice.warehouse.repositories;

import io.kirill.warehouseservice.warehouse.domain.StockLine;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface StockLineRepository extends ReactiveMongoRepository<StockLine, String> {
}
