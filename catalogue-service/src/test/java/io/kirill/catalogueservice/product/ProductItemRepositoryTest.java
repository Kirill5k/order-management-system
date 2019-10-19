package io.kirill.catalogueservice.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.test.StepVerifier;

@DataMongoTest
class ProductItemRepositoryTest {

  @Autowired
  ReactiveMongoTemplate template;

  @Autowired
  ProductItemRepository productItemRepository;

  @Test
  void findById() {
    var item = ProductItemBuilder.get().build();

    var foundItem = template.save(item)
        .map(ProductItem::getId)
        .flatMap(productItemRepository::findById);

    StepVerifier
        .create(foundItem)
        .expectNextMatches(i -> i.getId().equals(item.getId()) && i.getName().equals(item.getName()) && i.getPrice().equals(item.getPrice()))
        .verifyComplete();
  }
}