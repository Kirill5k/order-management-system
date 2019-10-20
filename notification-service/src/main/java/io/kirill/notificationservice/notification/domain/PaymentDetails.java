package io.kirill.notificationservice.notification.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class PaymentDetails {
  private final String cardType;
  private final String nameOnCard;
  private final String cardNumber;
  private final Integer cvv;
  private final String expires;
}
