package com.spring.reactive.example1.learn.flux;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class ExampleUnitTest {
    private static final String firstName = "Mohammad";
    private static final String secondName = "Elham";
    private static final String ERROR = "Error";


    @Test
    public void test1() {
        Flux<String> namesProvider = Flux.just(firstName, secondName);

        StepVerifier.create(namesProvider.log())
                .expectNext(firstName)
                .expectNext(secondName)
                .verifyComplete();
    }

    @Test
    public void testCount() {
        Flux<String> namesProvider = Flux.just(firstName, secondName);

        StepVerifier.create(namesProvider.log())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void testOnError() {
        Flux<String> namesProvider = Flux.just(firstName, secondName)
                .concatWith(Flux.error(new RuntimeException(ERROR)));

        StepVerifier.create(namesProvider.log())
                .expectNext(firstName)
                .expectNext(secondName)
                //.expectError(RuntimeException.class)
                .expectErrorMessage(ERROR)
                .verify();
    }
}
