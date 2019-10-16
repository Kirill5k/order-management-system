package io.kirill.warehouseservice.warehouse.listeners;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import io.kirill.warehouseservice.warehouse.WarehouseService;
import io.kirill.warehouseservice.warehouse.StockLine;
import io.kirill.warehouseservice.warehouse.exceptions.ItemNotFound;
import io.kirill.warehouseservice.warehouse.listeners.events.OrderLine;
import io.kirill.warehouseservice.warehouse.listeners.events.StockReservationEvent;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class StockEventsListenerTest {

  @Mock
  WarehouseService warehouseService;

  @InjectMocks
  StockEventsListener stockEventsListener;

  String itemId1 = "item-1";
  String itemId2 = "item-2";
  String orderId = "order-1";

  @Test
  void reserveStock() {
    var orderLines = List.of(new OrderLine(itemId1, 2), new OrderLine(itemId2, 2));
    var event = new StockReservationEvent(orderId, orderLines);

    doAnswer(inv -> Mono.empty()).when(warehouseService).verifyIsInStock(anyString(), anyInt());
    doAnswer(inv -> Mono.just(new StockLine(inv.getArgument(0), 2, 2)))
        .when(warehouseService).reserveStock(anyString(), anyInt());

    stockEventsListener.reserveStock(event);

    verify(warehouseService, timeout(500)).confirmStockReservation(orderId);
    verify(warehouseService, never()).declineStockReservation(anyString(), anyString());
    verify(warehouseService).verifyIsInStock(itemId1, 2);
    verify(warehouseService).verifyIsInStock(itemId2, 2);
    verify(warehouseService).reserveStock(itemId1, 2);
    verify(warehouseService).reserveStock(itemId2, 2);
  }

  @Test
  void reserveStockWhenNotInStock() {
    var orderLines = List.of(new OrderLine(itemId1, 2), new OrderLine(itemId2, 2));
    var event = new StockReservationEvent(orderId, orderLines);

    doAnswer(inv -> Mono.empty()).when(warehouseService).verifyIsInStock(itemId1, 2);
    doAnswer(inv -> Mono.error(new ItemNotFound(itemId2))).when(warehouseService).verifyIsInStock(itemId2, 2);

    stockEventsListener.reserveStock(event);

    verify(warehouseService, timeout(500)).declineStockReservation(orderId, "item with id item-2 does not exist");
    verify(warehouseService, after(500).never()).confirmStockReservation(anyString());
    verify(warehouseService, after(500).never()).reserveStock(anyString(), anyInt());
  }
}