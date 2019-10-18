package io.kirill.financeservice.finance;

import io.kirill.financeservice.finance.domain.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
