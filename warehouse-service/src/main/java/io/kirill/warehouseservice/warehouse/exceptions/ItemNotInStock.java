package io.kirill.warehouseservice.warehouse.exceptions;

public class ItemNotInStock extends RuntimeException {
  public ItemNotInStock(String itemId, int available, int required) {
    super(String.format("item %s is not in stock. available - %d, required - %d", itemId, available, required));
  }
}
