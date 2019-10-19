package io.kirill.warehouseservice.warehouse;

import io.kirill.warehouseservice.warehouse.domain.Order;
import io.kirill.warehouseservice.warehouse.domain.Shipment;
import io.kirill.warehouseservice.warehouse.repositories.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Predicate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

  @Mock
  ShipmentRepository shipmentRepository;

  @InjectMocks
  ShipmentService shipmentService;

  @Test
  void prepare() {
    var order = OrderBuilder.get().build();

    doAnswer(inv -> Mono.just(inv.getArgument(0))).when(shipmentRepository).save(any());

    var savedShipment = shipmentService.prepare(order);

    StepVerifier
      .create(savedShipment)
      .expectNextMatches(shipmentMatcher(order))
      .verifyComplete();

    verify(shipmentRepository).save(any());
  }

  Predicate<Shipment> shipmentMatcher(Order order) {
    return s -> s.getCustomerId().equals(order.getCustomerId())
      && s.getOrderId().equals(order.getId())
      && s.getShippingAddress().equals(s.getShippingAddress())
      && s.getDateCreated() != null;
  }
}