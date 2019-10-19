package io.kirill.warehouseservice.warehouse.listeners;

import io.kirill.warehouseservice.warehouse.WarehouseService;
import io.kirill.warehouseservice.warehouse.domain.Order;
import io.kirill.warehouseservice.warehouse.domain.OrderLine;
import io.kirill.warehouseservice.warehouse.domain.StockLine;
import io.kirill.warehouseservice.warehouse.exceptions.ItemNotFound;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
    var ol1 = new OrderLine(itemId1, 2);
    var ol2 = new OrderLine(itemId2, 2);
    var event = new Order(orderId, List.of(ol1, ol2));

    doAnswer(inv -> Mono.empty())
        .when(warehouseService).verifyIsInStock(any());
    doAnswer(inv -> Mono.just(new StockLine("item", 2, 2)))
        .when(warehouseService).reserveStock(any());

    stockEventsListener.reserveStock(event);

    verify(warehouseService, timeout(500)).confirmStockReservation(orderId);
    verify(warehouseService, never()).rejectStockReservation(anyString(), anyString());
    verify(warehouseService).verifyIsInStock(ol1);
    verify(warehouseService).verifyIsInStock(ol2);
    verify(warehouseService).reserveStock(ol1);
    verify(warehouseService).reserveStock(ol2);
  }

  @Test
  void reserveStockWhenNotInStock() {
    var ol1 = new OrderLine(itemId1, 2);
    var ol2 = new OrderLine(itemId2, 2);
    var event = new Order(orderId, List.of(ol1, ol2));

    doAnswer(inv -> Mono.empty()).when(warehouseService).verifyIsInStock(ol1);
    doAnswer(inv -> Mono.error(new ItemNotFound(itemId2))).when(warehouseService).verifyIsInStock(ol2);

    stockEventsListener.reserveStock(event);

    verify(warehouseService, timeout(500)).rejectStockReservation(orderId, "item with id item-2 does not exist");
    verify(warehouseService, after(500).never()).confirmStockReservation(anyString());
    verify(warehouseService, after(500).never()).reserveStock(any());
  }

  @Test
  void releaseStock() {
    var ol1 = new OrderLine(itemId1, 2);
    var ol2 = new OrderLine(itemId2, 2);
    var event = new Order(orderId, List.of(ol1, ol2));

    doAnswer(inv -> Mono.just(new StockLine("item", 2, 2)))
      .when(warehouseService).clearStockReservation(any());

    stockEventsListener.releaseStock(event);

    verify(warehouseService).clearStockReservation(ol1);
    verify(warehouseService).clearStockReservation(ol2);
  }
}