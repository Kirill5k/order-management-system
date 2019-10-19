package io.kirill.customerservice.customer;

import io.kirill.customerservice.customer.exceptions.CustomerNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final CustomerRepository customerRepository;

  public Mono<Customer> get(String id) {
    return customerRepository.findById(id)
        .switchIfEmpty(Mono.error(new CustomerNotFound(id)));
  }
}
