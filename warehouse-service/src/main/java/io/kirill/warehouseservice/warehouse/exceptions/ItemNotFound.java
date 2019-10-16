package io.kirill.warehouseservice.warehouse.exceptions;

public class ItemNotFound extends RuntimeException {
  public ItemNotFound(String itemId) {
    super(String.format("item with id %s does not exist", itemId));
  }
}
