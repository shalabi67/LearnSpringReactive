package com.spring.reactive.example1.webflux;


import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class FirstHandler {
    public Mono<ServerResponse> getNumbers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Flux.just(1, 2, 3, 4).log(), Integer.class);
    }

    public Mono<ServerResponse> getHighNumbers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(Flux.just(1000, 2000, 3000, 4000).delayElements(Duration.ofSeconds(1)).log(), Integer.class);
    }
}
