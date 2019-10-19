package io.kirill.warehouseservice.warehouse.listeners;

import io.kirill.warehouseservice.warehouse.OrderBuilder;
import io.kirill.warehouseservice.warehouse.ShipmentService;
import io.kirill.warehouseservice.warehouse.domain.Shipment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentEventsListenerTest {

  @Mock
  ShipmentService shipmentService;

  @InjectMocks
  ShipmentEventsListener shipmentEventsListener;

  @Test
  void dispatch() {
    var event = OrderBuilder.get().build();

    doAnswer(inv -> Mono.just(Shipment.builder().build()))
      .when(shipmentService).prepare(event);

    shipmentEventsListener.dispatch(event);

    verify(shipmentService, timeout(500)).prepare(event);
  }
}