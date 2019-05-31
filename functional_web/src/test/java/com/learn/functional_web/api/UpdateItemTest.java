package com.learn.functional_web.api;

import com.learn.functional_web.factory.ItemFactory;
import com.learn.functional_web.models.Item;
import com.learn.functional_web.repositories.ItemRepository;
import com.learn.functional_web.routers.ItemsRouter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@DirtiesContext
public class UpdateItemTest {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    ItemRepository itemRepository;

    @Before
    public void setup() {
        itemRepository.deleteAll().block();

        itemRepository.saveAll(Flux.fromIterable(ItemFactory.createItems(10))).blockLast();
        itemRepository.saveAll(Flux.fromIterable(ItemFactory.createItemsWithCommonDescription(3))).blockLast();

        Mono<Item> item1 = itemRepository.save(ItemFactory.create());
        item1.block();
    }

    @Test
    public void testUpdateExistingItem() {
        Item item = ItemFactory.create();
        webTestClient
                .put().uri(ItemsRouter.ITEM_URL,  "aaa")
                .accept(ItemsRouter.JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(item))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Item.class)
                .consumeWith(response -> {
                    Item item1 = response.getResponseBody();
                    Assert.assertEquals(item.getDescription(), item1.getDescription());
                    Assert.assertEquals(item.getPrice(), item1.getPrice(), .001);
                    Assert.assertEquals(item.getId(), item1.getId());
                });
    }

    @Test
    public void testUpdateExistingItem2() {
        Item item = ItemFactory.create();
        webTestClient
                .put().uri(ItemsRouter.ITEM_URL, "aaa")
                .accept(ItemsRouter.JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(item))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(item.getId())
                .jsonPath("$.description").isEqualTo(item.getDescription())
                .jsonPath("$.price").isEqualTo(item.getPrice());
    }

    @Test
    public void testUpdateNotFoundItem() {
        Item item = ItemFactory.create();
        webTestClient
                .put().uri(ItemsRouter.ITEM_URL, "qwe")
                .accept(ItemsRouter.JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(item))
                .exchange()
                .expectStatus().isNotFound();
    }
}
