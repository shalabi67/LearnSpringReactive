package com.reactive.client.client_controllers;

import com.reactive.client.models.Item;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/items")
public class ItemClientController {
    WebClient webClient = WebClient.create("http://localhost:8080");

    @GetMapping
    public Flux<Item> getItemsUsingRetrieve() {
        return webClient.get()
                .uri("/items")
                .retrieve()
                .bodyToFlux(Item.class)
                .log("Items from client project using retrieve");
    }

    @GetMapping("/v2")
    public Flux<Item> getItemsUsingExchange() {
        return webClient.get()
                .uri("/items")
                .exchange()
                .filter(result -> result.statusCode() == HttpStatus.OK)//this method returns the result and the response code
                .flatMapMany(result -> result.bodyToFlux(Item.class))
                .log("Items from client project using exchange");
    }

    @GetMapping(value = "/{id}")
    public Mono<String> getItem(@PathVariable String id) {
        ParameterizedTypeReference<ResponseEntity<Item>> parameterizedTypeReference = new ParameterizedTypeReference<ResponseEntity<Item>>() {
        };
        return webClient.get()
                .uri("/items/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .log("Get an Item using client project using retrieve");
    }

    @GetMapping("/v2/{id}")
    public Flux<Item> getItemUsingExchange(@PathVariable String id) {
        return webClient.get()
                .uri("/items/{id}", id)
                .exchange()
                .filter(result -> result.statusCode() == HttpStatus.OK)//this method returns the result and the response code
                .flatMapMany(result -> result.bodyToFlux(Item.class))
                .log("Get an Item using client project using exchange");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> addItem(@RequestBody Item item) {
        item.setId(null);
        Mono<Item> itemMono = Mono.just(item);
        return webClient.post()
                .uri("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemMono, Item.class)
                .retrieve()
                .bodyToMono(Item.class)
                .log("Post an Item using client project");
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<String> updateItem(@PathVariable String id, @RequestBody Item item) {
        Mono<Item> itemMono = Mono.just(item);
        return webClient.put()
                .uri("/items/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemMono, Item.class)
                .retrieve()
                .bodyToMono(String.class)
                .log("Put an Item using client project");
    }
}