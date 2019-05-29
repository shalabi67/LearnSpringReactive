package com.spring.reactive.example1.learn.flatmap;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class FlatMapFlux {
    private String[] namesArray = {"Mohammad", "Elham", "Nada", "Laith"};
    private List<String> names = Arrays.asList(namesArray);
    private Flux<String> namesPublisher = Flux.fromIterable(names)
            .log();

    @Test
    public void getNames() {
        System.out.println("get names length");
        Flux<String> modifiedPublisher = namesPublisher
                .flatMap(name -> createNewFlux(name))
                .log();

        modifiedPublisher
                .subscribe(System.out::println);
    }

    private Flux<String> createNewFlux(String name) {
        return Flux.fromArray(new String[] {name, name.toUpperCase(), name.toLowerCase()});
    }
}
