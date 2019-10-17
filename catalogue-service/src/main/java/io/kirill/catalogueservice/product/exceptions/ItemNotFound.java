package io.kirill.catalogueservice.product.exceptions;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import io.kirill.catalogueservice.common.exceptions.ServiceApiException;

public class ItemNotFound extends ServiceApiException {
  public ItemNotFound(String itemId) {
    super(NOT_FOUND, String.format("item with id %s does not exist", itemId));
  }
}
