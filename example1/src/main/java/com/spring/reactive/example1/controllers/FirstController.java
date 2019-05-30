package com.spring.reactive.example1.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@CrossOrigin
@RestController
@RequestMapping("/first")
public class FirstController {

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> getNumbers() {
        return Flux.range(1, 20)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/error", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> getNumbersWithError() {
        Flux<Integer> flux = Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .log();

        flux.concatWith(Flux.error(new RuntimeException("Error")));

        return flux;
    }
}
