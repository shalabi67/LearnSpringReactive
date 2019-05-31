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
public class DeleteItemTest {
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
    public void testAddItem() {
        Item item = ItemFactory.create();
        webTestClient
                .delete().uri(ItemsRouter.ITEMS_URL + "/aaa")
                .accept(ItemsRouter.JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Item.class);

    }

    @Test
    public void testDeleteNonExistingItem() {
        webTestClient
                .delete().uri(ItemsRouter.ITEMS_URL +"/er98")
                .accept(ItemsRouter.JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class);
    }
}
