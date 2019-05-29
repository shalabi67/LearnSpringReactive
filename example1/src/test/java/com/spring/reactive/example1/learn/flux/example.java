package com.spring.reactive.example1.learn.flux;

import org.junit.Test;
import reactor.core.publisher.Flux;

public class example {
    @Test
    public void example1() {
        Flux<String> names = Flux.just("Mohammad", "Elham", "Nada", "Laith")
                .log();
        names.subscribe(System.out::println);
    }

    /*
   shows how to add data.
    */
    @Test
    public void example2() {
        Flux<String> names = Flux.just("Mohammad", "Elham", "Nada", "Laith")
                .concatWithValues("you should  see me", "and me", "and me too")
                .log();
        names.subscribe(System.out::println,
                (e)-> System.out.println(e.getMessage()),
                () -> System.out.println("completed"));
    }

    /*
    shows how to emit errors and receive them.
     */
    @Test
    public void example3() {
        Flux<String> names = Flux.just("Mohammad", "Elham", "Nada", "Laith")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWithValues("you should not see me", "or me", "or me too")
                .log();
        names.subscribe(System.out::println,
                (e)-> System.out.println(e.getMessage()),
                () -> System.out.println("completed"));
    }
}
