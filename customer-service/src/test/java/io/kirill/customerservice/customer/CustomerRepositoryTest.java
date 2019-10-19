package io.kirill.customerservice.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;

@DataMongoTest
class CustomerRepositoryTest {

  @Autowired
  ReactiveMongoTemplate template;

  @Autowired
  CustomerRepository customerRepository;

  @Test
  void findById() {
    var customer = CustomerBuilder.get().build();

    var foundItem = template.save(customer)
        .map(Customer::getId)
        .flatMap(customerRepository::findById);

    StepVerifier
        .create(foundItem)
        .expectNextMatches(i -> i.getId().equals(customer.getId()) && i.getFirstName().equals(customer.getFirstName()) && i.getEmail().equals(customer.getEmail()))
        .verifyComplete();
  }
}