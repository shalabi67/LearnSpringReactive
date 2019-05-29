package com.spring.reactive.example1.learn.hot_cold;

import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class HotColdPublishers {
    private String[] namesArray = {"Mohammad", "Elham", "Nada", "Laith"};
    private List<String> names = Arrays.asList(namesArray);
    Flux<String> namesPublisher = Flux.fromIterable(names)
            .delayElements(Duration.ofSeconds(1L));
            //.log();

    @Test
    public void coldPublisher() throws InterruptedException {
        //cold publisher will emmit from start
        namesPublisher.subscribe(name -> System.out.println("Subscriber1: " + name));
        Thread.sleep(2000);

        namesPublisher.subscribe(name -> System.out.println("Subscriber2: " + name));
        Thread.sleep(1000);

        namesPublisher.subscribe(name -> System.out.println("Subscriber3: " + name));
        Thread.sleep(5000);
    }

    @Test
    public void hotPublisher() throws InterruptedException {
        //hot publisher will emmit from subscription time
        ConnectableFlux<String> hotPublisher = namesPublisher.publish();
        hotPublisher.connect();

        hotPublisher.subscribe(name -> System.out.println("Subscriber1: " + name));
        Thread.sleep(2000);

        hotPublisher.subscribe(name -> System.out.println("Subscriber2: " + name));
        Thread.sleep(1000);

        hotPublisher.subscribe(name -> System.out.println("Subscriber3: " + name));
        Thread.sleep(5000);
    }
}
