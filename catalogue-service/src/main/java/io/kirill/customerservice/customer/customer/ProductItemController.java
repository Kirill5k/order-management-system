package io.kirill.customerservice.customer.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductItemController {
  private final ProductItemService productItemService;

  @GetMapping("/{id}")
  public Mono<ProductItem> getOne(@PathVariable String id) {
    return productItemService.get(id);
  }
}
