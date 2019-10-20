package io.kirill.notificationservice.notification.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotEmpty;

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
