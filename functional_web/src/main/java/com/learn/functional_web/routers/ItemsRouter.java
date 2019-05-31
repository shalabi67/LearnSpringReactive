package com.learn.functional_web.routers;

import com.learn.functional_web.handlers.ItemsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ItemsRouter {
    public static final String ITEMS_URL = "/items";
    private static final String ITEM_ID = "/{id}";
    public static final String ITEM_URL = ITEMS_URL + ITEM_ID;
    public static final MediaType TEXT_STREAM = MediaType.TEXT_EVENT_STREAM;
    public static final MediaType JSON = MediaType.APPLICATION_JSON_UTF8;

    @Bean
    public RouterFunction<ServerResponse> getItemsRoute(ItemsHandler itemsHandler) {
        return RouterFunctions
                .route(getItemsRoute(), itemsHandler::getAllItems)
                .andRoute(getItemRoute(), itemsHandler::getItem)
                .andRoute(deleteItemRoute(), itemsHandler::deleteItem)
                .andRoute(updateItemRoute(), itemsHandler::updateItem)
                .andRoute(addItemRoute(), itemsHandler::addItem);


    }

    private RequestPredicate getItemsRoute() {
        return RequestPredicates.GET(ITEMS_URL)
                .and(RequestPredicates.accept(TEXT_STREAM));
    }

    private RequestPredicate getItemRoute() {
        return RequestPredicates.GET(ITEM_URL)
                .and(RequestPredicates.accept(TEXT_STREAM));
    }
    private RequestPredicate addItemRoute() {
        return RequestPredicates.POST(ITEMS_URL)
                                .and(RequestPredicates.accept(JSON));

    }
    private RequestPredicate deleteItemRoute() {
        return RequestPredicates.DELETE(ITEM_URL)
                .and(RequestPredicates.accept(JSON));

    }
    private RequestPredicate updateItemRoute() {
        return RequestPredicates.PUT(ITEM_URL)
                .and(RequestPredicates.accept(JSON));

    }
}
