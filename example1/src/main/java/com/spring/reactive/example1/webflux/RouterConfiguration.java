package com.spring.reactive.example1.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouterConfiguration {

    @Bean
    RouterFunction<ServerResponse> numbersRout(FirstHandler firstHandler) {
        return RouterFunctions
                .route(
                    GET("/numbers").and(accept(MediaType.APPLICATION_JSON)),
                        firstHandler::getNumbers
                )
                .andRoute(GET("/high").and(accept(MediaType.APPLICATION_STREAM_JSON)),
                        firstHandler::getHighNumbers
                );
    }
}
