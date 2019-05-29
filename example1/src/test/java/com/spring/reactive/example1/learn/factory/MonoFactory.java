package com.spring.reactive.example1.learn.factory;

import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public class MonoFactory {
    private String name = "Mohammad";
    private static int counter = 0;

    @Test
    public void createEmptyMono() {
        Mono<String> emptyMono = Mono.empty();

        emptyMono
                .log()
                .subscribe(System.out::println);
    }

    @Test
    public void createMonoOrEmptyMono() {
        Mono<String> monoPublisher = Mono.justOrEmpty(null);

        System.out.println("Empty Mono Example");
        monoPublisher
                .log()
                .subscribe(System.out::println);

        monoPublisher = Mono.justOrEmpty(name);

        System.out.println("Not Empty Mono Example");
        monoPublisher
                .log()
                .subscribe(System.out::println);
    }

    @Test
    public void createMonoFromSupplier() {

        Mono<String> monoPublisher = Mono.fromSupplier(() -> name);

        monoPublisher
                .log()
                .subscribe(System.out::println);

        Supplier<String> nameSupplier = () -> {
            counter++;
            return counter % 2 == 0 ? name : null;
        };

        monoPublisher = Mono.fromSupplier(nameSupplier);

        for (int i = 0; i < 4; i++) {
            System.out.println("###################  Next call ######################");
            monoPublisher
                    .log()
                    .subscribe(System.out::println);
        }
    }
}
