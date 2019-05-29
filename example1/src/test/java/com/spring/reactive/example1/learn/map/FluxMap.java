package com.spring.reactive.example1.learn.map;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class FluxMap {
    private String[] namesArray = {"Mohammad", "Elham", "Nada", "Laith"};
    private List<String> names = Arrays.asList(namesArray);
    private Flux<String> namesPublisher = Flux.fromIterable(names)
            .log();

    @Test
    public void getNamesLength() {
        System.out.println("get names length");
        Flux<Integer> namesLength = namesPublisher
                .map(name -> name.length())
                .log();

        namesLength
                .subscribe(System.out::println);
    }

    @Test
    public void getUpperCaseNames() {
        System.out.println("get names length");
        namesPublisher
                .map(name -> name.toUpperCase())
                .log()
                .subscribe(System.out::println);

    }
}
