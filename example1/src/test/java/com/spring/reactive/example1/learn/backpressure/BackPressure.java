package com.spring.reactive.example1.learn.backpressure;

import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

public class BackPressure {
    private String[] namesArray = {"Mohammad", "Elham", "Nada", "Laith"};
    private List<String> names = Arrays.asList(namesArray);
    private Flux<String> namesPublisher = Flux.fromIterable(names)
            .log();

    @Test
    public void getTwoNames() {
        namesPublisher.subscribe(System.out::println, System.out::println, System.out::println,
                subscription -> subscription.request(2));

        System.out.println("################  Another way ####################");
        namesPublisher.subscribe(getTwoElementsSubscriber());
        //notice there will be no oncomplete.
    }

    @Test
    public void testCancelOnNada() {
        namesPublisher.subscribe(cancelOnNada());
        //notice there will be no oncomplete.
    }

    private Subscriber<String> getTwoElementsSubscriber() {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(2L);
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable);
            }

            @Override
            public void onComplete() {
                System.out.println("Completed");
            }
        };

        return subscriber;
    }

    private Subscriber<String> cancelOnNada() {
        BaseSubscriber<String> baseSubscriber = new BaseSubscriber() {
            @Override
            protected void hookOnNext(Object value) {
                super.hookOnNext(value);
                if (value == "Nada") {
                    cancel();
                    return;
                }

                System.out.println(value);
            }
        };

        return baseSubscriber;
    }
}
