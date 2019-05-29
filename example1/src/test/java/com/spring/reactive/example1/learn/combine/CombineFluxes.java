package com.spring.reactive.example1.learn.combine;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class CombineFluxes {
    private String[] namesArray = {"Mohammad", "Elham", "Nada", "Laith"};
    private List<String> names = Arrays.asList(namesArray);
    private Flux<String> namesPublisher = Flux.fromIterable(names)
            .log();

    @Test
    public void getNamesLength() {
        Flux<String> namesUpperCase = namesPublisher
                .map(name -> name.toUpperCase())
                .log();

        Flux<String> mergedFlux = Flux.merge(namesPublisher, namesUpperCase);

        mergedFlux.subscribe(System.out::println);
    }

    @Test
    public void combineUsingMerge() {

        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergedFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(mergedFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }

    @Test
    public void combineUsingMerge_withDelay() {

        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));
        ;

        Flux<String> mergedFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(mergedFlux.log())
                .expectSubscription()
                .expectNextCount(6)
                //.expectNext("A","B","C","D","E","F")
                .verifyComplete();

    }

    @Test
    public void combineUsingConcat() {

        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergedFlux = Flux.concat(flux1, flux2);

        StepVerifier.create(mergedFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }

    @Test
    public void combineUsingConcat_withDelay() {

        VirtualTimeScheduler.getOrSet();

        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.concat(flux1, flux2);

        StepVerifier.withVirtualTime(() -> mergedFlux.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(6))
                .expectNextCount(6)
                .verifyComplete();


      /*  StepVerifier.create(mergedFlux.log())
                .expectSubscription()
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();*/

    }

    @Test
    public void combineUsingZip() {

        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> mergedFlux = Flux.zip(flux1, flux2, (t1, t2) -> {
            return t1.concat(t2); // AD, BE, CF
        }); //A,D : B,E : C:F

        StepVerifier.create(mergedFlux.log())
                .expectSubscription()
                .expectNext("AD", "BE", "CF")
                .verifyComplete();

    }
}
