package io.kirill.warehouseservice.warehouse.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
@Builder
@RequiredArgsConstructor
public class Address {
  @NotEmpty
  private final String line1;
  private final String line2;
  @NotEmpty
  private final String city;
  private final String county;
  @NotEmpty
  private final String postcode;
  @NotEmpty
  private final String country;
}
