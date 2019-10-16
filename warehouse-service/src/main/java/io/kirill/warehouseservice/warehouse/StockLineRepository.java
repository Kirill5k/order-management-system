package io.kirill.warehouseservice.warehouse;

import io.kirill.warehouseservice.warehouse.domain.StockLine;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface StockLineRepository extends ReactiveMongoRepository<StockLine, String> {
}
