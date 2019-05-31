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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@DirtiesContext
public class GetAllItemsTest {
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
    public void testGetAllItems() {
        Flux<Item> items = webTestClient
                .get().uri(ItemsRouter.ITEMS_URL)
                .accept(ItemsRouter.TEXT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(items.log())
                .expectSubscription()
                .expectNextCount(10 + 3 + 1)
                .verifyComplete();
    }

    @Test
    public void testGetAllItems2() {
        webTestClient
                .get().uri(ItemsRouter.ITEMS_URL)
                .accept(ItemsRouter.TEXT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Item.class)
                .consumeWith(response -> {
                    List<Item> items = response.getResponseBody();
                    for (Item item : items) {
                        Assert.assertNotNull(item.getId());
                    }
                });

    }
}
