package io.kirill.warehouseservice.warehouse;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface StockLineRepository extends ReactiveMongoRepository<StockLine, String> {
}
