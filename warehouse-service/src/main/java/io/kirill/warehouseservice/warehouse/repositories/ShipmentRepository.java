package io.kirill.warehouseservice.warehouse.repositories;

import io.kirill.warehouseservice.warehouse.domain.Shipment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ShipmentRepository extends ReactiveMongoRepository<Shipment, String> {
}
