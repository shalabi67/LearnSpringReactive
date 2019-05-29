package com.learn.example2.controllers;

import com.learn.example2.factory.ItemFactory;
import com.learn.example2.models.Item;
import com.learn.example2.repositories.ItemRepository;
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
public class ItemControllerTest {

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
        Flux<Item> items =webTestClient
                .get().uri(ItemController.ITEMS_URL)
                .accept(MediaType.APPLICATION_STREAM_JSON)
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
    public void testAddItem() {
        Item item = ItemFactory.create();
        Flux<Item> items =webTestClient
                .post().uri(ItemController.ITEMS_URL)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(item))
                .exchange()
                .expectStatus().isOk()
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(items.log())
                .expectSubscription()
                .assertNext(item1 -> Assert.assertEquals("description aaa", item1.getDescription()))
                //.expectNextCount(1)
                .verifyComplete();
    }
}
