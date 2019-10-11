package io.kirill.orderservice.order.domain;

import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class Address {
  private final String line1;
  private final String line2;
  private final String city;
  private final String county;
  private final String postcode;
  private final String country;
}
