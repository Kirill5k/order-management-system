package io.kirill.catalogueservice.product;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Builder
@Document
@RequiredArgsConstructor
public class ProductItem {
  @Id
  private final String id;
  private final BigDecimal price;
  private final String name;
  private final String description;
}
