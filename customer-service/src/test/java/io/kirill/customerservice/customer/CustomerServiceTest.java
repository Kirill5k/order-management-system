package io.kirill.customerservice.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

  @Mock
  CustomerRepository customerRepository;

  @InjectMocks
  CustomerService customerService;

  String customerId = "customer-1";
  Customer customer = CustomerBuilder.get().id(customerId).build();

  @Test
  void get() {
    when(customerRepository.findById(customerId)).thenReturn(Mono.just(customer));

    StepVerifier
        .create(customerService.get(customerId))
        .expectNextMatches(i -> i.getId().equals(customer.getId()))
        .verifyComplete();

    verify(customerRepository).findById(customerId);
  }

  @Test
  void getWhenNotFound() {
    when(customerRepository.findById(customerId)).thenReturn(Mono.empty());

    StepVerifier
        .create(customerService.get(customerId))
        .verifyErrorMatches(error -> error.getMessage().equals("customer with id customer-1 does not exist"));

    verify(customerRepository).findById(customerId);
  }
}