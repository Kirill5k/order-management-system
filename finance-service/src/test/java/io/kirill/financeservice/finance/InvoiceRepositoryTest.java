package io.kirill.financeservice.finance;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

@DataMongoTest
class InvoiceRepositoryTest {

  @Autowired
  InvoiceRepository invoiceRepository;

  @Test
  void save() {
    var transaction = InvoiceBuilder.get().id(null).build();

    StepVerifier
        .create(invoiceRepository.save(transaction))
        .expectNextMatches(tr -> tr.getId() != null)
        .verifyComplete();
  }
}