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
public class CreateItemTest {
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
    public void testDeleteItem() {
        Item item = ItemFactory.create();
        Flux<Item> items = webTestClient
                .post().uri(ItemsRouter.ITEMS_URL)
                .accept(ItemsRouter.JSON)
                .contentType(ItemsRouter.JSON)
                .body(BodyInserters.fromObject(item))
                .exchange()
                .expectStatus().isCreated()
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(items.log())
                .expectSubscription()
                .assertNext(item1 -> Assert.assertEquals("description aaa", item1.getDescription()))
                //.expectNextCount(1)
                .verifyComplete();
    }

    //notice this api has media type of APPLICATION_STREAM_JSON_VALUE not like the other apis
    @Test
    public void testAddItem2() {
        Item item = ItemFactory.create();
        webTestClient
                .post().uri(ItemsRouter.ITEMS_URL)
                .accept(ItemsRouter.JSON)
                .contentType(ItemsRouter.JSON)
                .body(BodyInserters.fromObject(item))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Item.class)
                .consumeWith(response -> {
                    Item item1 = response.getResponseBody();
                    Assert.assertEquals(item.getDescription(), item1.getDescription());
                    Assert.assertEquals(item.getPrice(), item1.getPrice(), .001);
                    Assert.assertNotNull(item1.getId());
                    Assert.assertNotEquals(item.getId(), item1.getId());
                });
    }
}
