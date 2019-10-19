package io.kirill.customerservice.customer;

import io.kirill.customerservice.customer.Customer;

public class CustomerBuilder {
  public static Customer.CustomerBuilder get() {
    return Customer.builder()
        .id("customer-1")
        .firstName("Donald")
        .lastName("Trump")
        .email("donaltrump@whitehouse.com");
  }
}
