package com.learn.example2.repositories;

import com.learn.example2.factory.ItemFactory;
import com.learn.example2.models.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

//@DataMongoTest
@RunWith(SpringRunner.class)
@SpringBootTest
//@AutoConfigureWebTestClient
@DirtiesContext
public class ItemRepositoryTest {
    private static int count = 10;

    @Autowired
    private ItemRepository itemRepository;

    @Before
    public void setup() {
        itemRepository.deleteAll().block();

        itemRepository.saveAll(Flux.fromIterable(ItemFactory.createItems(count))).blockLast();
        itemRepository.saveAll(Flux.fromIterable(ItemFactory.createItemsWithCommonDescription(3))).blockLast();

        Mono<Item> item1 = itemRepository.save(ItemFactory.create());
        item1.block();
    }

    @Test
    public void getAllItems() {
        Flux<Item> items = itemRepository.findAll();

        StepVerifier.create(items.log())
                .expectSubscription()
                .expectNextCount(count + 3 + 1)
                .expectComplete()
                .verify();  //this is a must
    }

    @Test
    public void getItemsById() {
        StepVerifier.create(itemRepository.findById("aaa").log())
                .expectSubscription()
                .expectNextMatches(inputItem -> inputItem.getId().equals("aaa"))
                .verifyComplete();
    }

    @Test
    public void getAllItemsWithDescription() {
        Flux<Item> items = itemRepository.findByDescription("description");

        StepVerifier.create(items.log())
                .expectSubscription()
                .assertNext(item -> Assert.assertEquals("description", item.getDescription()))
                .expectNextCount(2)
                .verifyComplete();
    }

}
