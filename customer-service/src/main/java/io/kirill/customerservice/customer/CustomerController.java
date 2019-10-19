package io.kirill.customerservice.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
  private final CustomerService customerService;

  @GetMapping("/{id}")
  public Mono<Customer> getOne(@PathVariable String id) {
    return customerService.get(id);
  }
}
