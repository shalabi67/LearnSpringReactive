package com.spring.reactive.example1.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController("/first")
public class FirstController {

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> getNumbers() {
        return Flux.range(1, 20)
                .delayElements(Duration.ofSeconds(1))
                .log();
    }
}
