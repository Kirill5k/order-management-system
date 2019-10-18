package io.kirill.financeservice.finance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

@DataMongoTest
class TransactionRepositoryTest {

  @Autowired
  TransactionRepository transactionRepository;

  @Test
  void save() {
    var transaction = TransactionBuilder.get().id(null).build();

    StepVerifier
        .create(transactionRepository.save(transaction))
        .expectNextMatches(tr -> tr.getId() != null)
        .verifyComplete();
  }
}