package io.kirill.customerservice.customer;

import io.kirill.customerservice.customer.exceptions.CustomerNotFound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest(CustomerController.class)
class CustomerControllerTest {

  @Autowired
  WebTestClient client;

  @MockBean
  CustomerService customerService;

  String customerId = "customer-1";
  Customer customer = CustomerBuilder.get().id(customerId).build();

  @Test
  void getOne() {
    doAnswer(invocation -> Mono.just(customer))
        .when(customerService)
        .get(customerId);

    client
        .get()
        .uri("/customers/customer-1")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentTypeCompatibleWith(APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.id").isEqualTo(customerId);

    verify(customerService).get(customerId);
  }

  @Test
  void getOneWhenNotFound() {
    doAnswer(invocation -> Mono.error(new CustomerNotFound(customerId)))
        .when(customerService)
        .get(customerId);

    client
        .get()
        .uri("/customers/customer-1")
        .exchange()
        .expectStatus().isNotFound()
        .expectHeader().contentTypeCompatibleWith(APPLICATION_JSON)
        .expectBody()
        .jsonPath("$.message").isEqualTo("customer with id customer-1 does not exist");

    verify(customerService).get(customerId);
  }
}