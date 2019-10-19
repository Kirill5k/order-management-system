package io.kirill.warehouseservice.warehouse;

import io.kirill.warehouseservice.warehouse.domain.Order;
import io.kirill.warehouseservice.warehouse.domain.Shipment;
import io.kirill.warehouseservice.warehouse.repositories.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ShipmentService {
  private final ShipmentRepository shipmentRepository;

  public Mono<Shipment> prepare(Order order) {
    return shipmentRepository.save(order.toShipment());
  }
}
