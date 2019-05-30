package com.learn.example2.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.example2.factory.ItemFactory;
import com.learn.example2.models.Item;
import com.learn.example2.repositories.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

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
        Flux<Item> items = webTestClient
                .get().uri(ItemController.ITEMS_URL)
                .accept(MediaType.TEXT_EVENT_STREAM)
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
                .get().uri(ItemController.ITEMS_URL)
                .accept(MediaType.TEXT_EVENT_STREAM)
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

    @Test
    public void testAddItem() {
        Item item = ItemFactory.create();
        Flux<Item> items = webTestClient
                .post().uri(ItemController.ITEMS_URL)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .contentType(MediaType.APPLICATION_JSON)
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
                .post().uri(ItemController.ITEMS_URL)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .contentType(MediaType.APPLICATION_JSON)
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

    @Test
    public void testGetExistingItem() {
        ParameterizedTypeReference<ResponseEntity<Item>> typeRef =
                new ParameterizedTypeReference<ResponseEntity<Item>>() {
                };
        webTestClient
                .get().uri(ItemController.ITEMS_URL + "/aaa")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String result = new String(response.getResponseBody()).substring(5).trim();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        Item item = mapper.readValue(result, Item.class);
                        Assert.assertEquals("aaa", item.getId());
                        Assert.assertEquals(99.59, item.getPrice(), .001);
                    } catch (IOException e) {
                        Assert.fail();
                    }

                });

        //.jsonPath("$.id").isEqualTo("aaa")
        //.jsonPath("$.price").isEqualTo(99.59);

    }

    @Test
    public void testGetNotFoundItem() {
        webTestClient
                .get().uri(ItemController.ITEMS_URL + "/qwe")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isNotFound();

    }

    //notice this api has media type of APPLICATION_STREAM_JSON_VALUE not like the other apis
    @Test
    public void testUpdateExistingItem() {
        Item item = ItemFactory.create();
        webTestClient
                .put().uri(ItemController.ITEMS_URL + "/aaa")
                .accept(MediaType.APPLICATION_STREAM_JSON)
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

    //notice this api has media type of APPLICATION_STREAM_JSON_VALUE not like the other apis
    @Test
    public void testUpdateExistingItem2() {
        Item item = ItemFactory.create();
        webTestClient
                .put().uri(ItemController.ITEMS_URL + "/aaa")
                .accept(MediaType.APPLICATION_STREAM_JSON)
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
                .put().uri(ItemController.ITEMS_URL + "/qwe")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(item))
                .exchange()
                .expectStatus().isNotFound();
    }
}
