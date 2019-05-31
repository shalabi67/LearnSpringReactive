package com.learn.functional_web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@DirtiesContext
public class GetItemTests {
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
    public void testGetExistingItem() {
        ParameterizedTypeReference<ResponseEntity<Item>> typeRef =
                new ParameterizedTypeReference<ResponseEntity<Item>>() {
                };
        webTestClient
                .get().uri(ItemsRouter.ITEMS_URL + "/aaa")
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
                .get().uri(ItemsRouter.ITEMS_URL + "/qwe")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isNotFound();

    }
}
