package io.kirill.customerservice.customer;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Builder
@Document
@RequiredArgsConstructor
public class Customer {
  @Id
  private final String id;
  private final String firstName;
  private final String lastName;
  private final String email;
}
