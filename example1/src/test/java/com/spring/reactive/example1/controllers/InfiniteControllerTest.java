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
public class InfiniteControllerTest {
    private static final String URL = "/infinite";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getNumbersTest() {
        Flux<Long> numberPublisher = webTestClient
                .get()
                .uri(URL)
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(numberPublisher)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .thenCancel()
                .verify();
    }
}
