package com.spring.reactive.example1.learn.factory;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class FluxFactory {
    private String[] namesArray = {"Mohammad", "Elham", "Nada", "Laith"};
    private List<String> names = Arrays.asList(namesArray);

    @Test
    public void createFromIterable() {
        Flux<String> namesPublisher = Flux.fromIterable(names);

        namesPublisher.subscribe(System.out::println);
    }

    @Test
    public void createFromArray() {
        Flux<String> namesPublisher = Flux.fromArray(namesArray);

        namesPublisher.subscribe(System.out::println);
    }

    @Test
    public void createFromStream() {
        Flux<String> namesPublisher = Flux.fromStream(names.stream());

        namesPublisher.subscribe(System.out::println);
    }

    @Test
    public void createFromRange() {
        Flux<Integer> namesPublisher = Flux.range(100, 5);

        namesPublisher.subscribe(System.out::println);
    }
}
