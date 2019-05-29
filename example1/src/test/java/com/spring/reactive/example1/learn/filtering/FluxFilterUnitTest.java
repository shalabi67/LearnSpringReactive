package com.spring.reactive.example1.learn.filtering;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxFilterUnitTest {
    private String[] namesArray = {"Mohammad", "Elham", "Nada", "Laith"};
    private List<String> names = Arrays.asList(namesArray);

    @Test
    public void createFromIterable() {
        Flux<String> namesPublisher = Flux.fromIterable(names);

        System.out.println("get ll names with length equals to 5");
        Flux<String> filterPublisher = namesPublisher
                .filter(name -> name.length() == 5)
                .log();


        StepVerifier.create(filterPublisher)
                .expectNext("Elham", "Laith")
                .verifyComplete();
    }
}
