package io.kirill.warehouseservice.warehouse.listeners;

import static io.kirill.warehouseservice.common.configs.KafkaConfig.WAREHOUSE_STOCK_RESERVE_TOPIC;

import io.kirill.warehouseservice.warehouse.WarehouseService;
import io.kirill.warehouseservice.warehouse.listeners.events.StockReservationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockEventsListener {
  private final WarehouseService warehouseService;

  @KafkaListener(topics = WAREHOUSE_STOCK_RESERVE_TOPIC)
  public void reserveStock(@Payload Object stockReservationEvent) {
    var event = (StockReservationEvent) stockReservationEvent;
    log.info("received stock reservation event for order {}", event.getOrderId());
    Flux.fromIterable(event.getOrderLines())
        .flatMap(ol -> warehouseService.verifyIsInStock(ol.getItemId(), ol.getQuantity()))
        .thenMany(Flux.fromIterable(event.getOrderLines()))
        .doOnNext(ol -> warehouseService.reserveStock(ol.getItemId(), ol.getQuantity()))
        .onErrorStop()
        .doOnError(error -> warehouseService.rejectStockReservation(event.getOrderId(), error.getMessage()))
        .doOnComplete(() -> warehouseService.confirmStockReservation(event.getOrderId()))
        .subscribe();
  }
}
