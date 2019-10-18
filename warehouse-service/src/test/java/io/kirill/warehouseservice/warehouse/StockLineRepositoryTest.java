package io.kirill.warehouseservice.warehouse;

import io.kirill.warehouseservice.warehouse.domain.StockLine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;

@DataMongoTest
class StockLineRepositoryTest {

  @Autowired
  private ReactiveMongoTemplate template;

  @Autowired
  private StockLineRepository stockLineRepository;

  @Test
  void findByOrderId() {
    var stockLine = new StockLine("1", 1, 1);

    var foundStockLine = template.save(stockLine)
        .flatMap(sl -> stockLineRepository.findById(stockLine.getItemId()));

    StepVerifier
        .create(foundStockLine)
        .expectNextMatches(sl -> sl.getItemId().equals(stockLine.getItemId()) && sl.getAmountAvailable().equals(stockLine.getAmountAvailable()))
        .verifyComplete();
  }

  @Test
  void findByOrderIdWhenNotFound() {
    StepVerifier
        .create(stockLineRepository.findById("1"))
        .verifyComplete();
  }
}