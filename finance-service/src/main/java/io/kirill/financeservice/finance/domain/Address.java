package io.kirill.financeservice.finance.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class Address {
  private final String line1;
  private final String line2;
  private final String city;
  private final String county;
  private final String postcode;
  private final String country;
}
