package io.kirill.orderservice.order.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class PaymentDetails {
  @NotEmpty
  private final String cardType;
  @NotEmpty
  private final String nameOnCard;
  @Pattern(regexp = "^4[0-9]{12}(?:[0-9]{3})?$")
  private final String cardNumber;
  @Min(100)
  @Max(999)
  private final Integer cvv;
  @Pattern(regexp = "^(0[1-9]|1[0-2])\\/?([0-9]{4}|[0-9]{2})$")
  private final String expires;
}
