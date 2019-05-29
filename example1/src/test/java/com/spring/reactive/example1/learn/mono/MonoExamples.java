package com.spring.reactive.example1.learn.mono;

import org.junit.Test;
import reactor.core.publisher.Mono;

public class MonoExamples {
    @Test
    public void example1() {
        Mono<String> nameMono = Mono.just("Mohammad")
                .log();

        nameMono.subscribe(System.out::println);
    }

    @Test
    public void exampleError() {
        Mono<Object> nameMono = Mono.error(new RuntimeException("Error"))
                .log();

        nameMono.subscribe(System.out::println,
                (e)->System.out.println(e));
    }
}
