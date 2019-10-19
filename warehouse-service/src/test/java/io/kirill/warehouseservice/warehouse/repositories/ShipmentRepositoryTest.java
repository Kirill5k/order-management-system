package io.kirill.warehouseservice.warehouse.repositories;

import io.kirill.warehouseservice.warehouse.domain.Shipment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;

@DataMongoTest
class ShipmentRepositoryTest {

  @Autowired
  private ReactiveMongoTemplate template;

  @Autowired
  private ShipmentRepository shipmentRepository;

  @Test
  void save() {
    var shipment = new Shipment(null, "order-1", "customer-1", null, null, null);

    var savedShipment = shipmentRepository.save(shipment)
      .map(Shipment::getId)
      .flatMap(id -> template.findById(id, Shipment.class));

    StepVerifier
        .create(savedShipment)
        .expectNextMatches(s -> s.getId() != null && s.getCustomerId().equals("customer-1"))
        .verifyComplete();
  }
}