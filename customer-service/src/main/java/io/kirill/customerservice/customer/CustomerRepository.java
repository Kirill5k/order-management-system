package io.kirill.customerservice.customer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
