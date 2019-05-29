package com.learn.example2.controllers;

import com.learn.example2.models.Item;
import com.learn.example2.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.*;

@RestController
@RequestMapping(ItemController.ITEMS_URL)
@Slf4j
public class ItemController {
    public static final String ITEMS_URL = "/items";

    private ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Item> getItems() {
        return itemRepository.findAll();
    }

    @PostMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<Item> addItem(@RequestBody Item item) {
        item.setId(null);
        return itemRepository.save(item);
    }
}
