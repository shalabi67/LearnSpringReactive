package com.learn.reactive.server_side_events.controllers;

import com.learn.reactive.server_side_events.modules.Item;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.time.Duration;

@RestController
@RequestMapping(TimeController.TIME_URL)
public class TimeController {
    public static final String TIME_URL = "/time";

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Item> getItems(ServerHttpResponse response) {
        /*
        Access-Control-Allow-Origin: <your-domain-here>
Access-Control-Expose-Headers: *
Access-Control-Allow-Credentials: true
         */
        response.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:8081");
        response.getHeaders().add("Access-Control-Expose-Headers", "*");
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        return Flux.interval(Duration.ofSeconds(1))
                .map(time -> new Item(null, "interval item " + time, time + 1.00));
    }
}
