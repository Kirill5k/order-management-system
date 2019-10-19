package io.kirill.catalogueservice.product;

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
class ProductItemServiceTest {

  @Mock
  ProductItemRepository productItemRepository;

  @InjectMocks
  ProductItemService productItemService;

  @Test
  void get() {
    var id = "item-1";
    var product = ProductItemBuilder.get().id(id).build();

    when(productItemRepository.findById(id)).thenReturn(Mono.just(product));

    StepVerifier
        .create(productItemService.get(id))
        .expectNextMatches(i -> i.getId().equals(product.getId()))
        .verifyComplete();

    verify(productItemRepository).findById(id);
  }

  @Test
  void getWhenNotFound() {
    var id = "item-1";

    when(productItemRepository.findById(id)).thenReturn(Mono.empty());

    StepVerifier
        .create(productItemService.get(id))
        .verifyErrorMatches(error -> error.getMessage().equals("item with id item-1 does not exist"));

    verify(productItemRepository).findById(id);
  }
}