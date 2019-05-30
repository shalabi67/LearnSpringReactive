package com.learn.example2.controllers;

import com.learn.example2.models.Item;
import com.learn.example2.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Item> getItems() {
        return itemRepository.findAll();
    }

    @PostMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> addItem(@RequestBody Item item) {
        item.setId(null);
        return itemRepository.save(item);
    }

    @GetMapping(value="/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<ResponseEntity<Item>> getItem(@PathVariable String id) {
        return itemRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(value="/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id, @RequestBody Item item) {
        return itemRepository.findById(id)
                .flatMap(existingItem -> {
                    item.setId(id);
                    return itemRepository.save(item);
                })
                .map(i -> new ResponseEntity<>(i, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
