package com.spring.reactive.example1.webflux;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class NumbersTest {
    private static String URL = "/numbers";

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void numbersTest() {
        Flux<Integer> numberPublisher = webTestClient
                .get()
                .uri(URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(numberPublisher)
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }
}
