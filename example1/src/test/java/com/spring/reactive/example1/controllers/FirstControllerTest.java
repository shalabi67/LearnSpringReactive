package com.spring.reactive.example1.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


@RunWith(SpringRunner.class)
@WebFluxTest
public class FirstControllerTest {
    private static final String URL = "/first";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getNumbersTest() {
        Flux<Integer> numberPublisher = webTestClient
                .get()
                .uri(URL)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(numberPublisher)
                .expectSubscription()
                .expectNextCount(20)
                .verifyComplete();
    }
}
