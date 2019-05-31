package com.learn.functional_web.handlers;

import com.learn.functional_web.models.Item;
import com.learn.functional_web.repositories.ItemRepository;
import com.learn.functional_web.routers.ItemsRouter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemsHandler {

    private ItemRepository itemRepository;

    public ItemsHandler(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(ItemsRouter.TEXT_STREAM)
                .body(itemRepository.findAll(), Item.class);
    }

    public Mono<ServerResponse> getItem(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return itemRepository.findById(id)
                .flatMap(item -> {
                    return ServerResponse
                            .ok()
                            .contentType(ItemsRouter.TEXT_STREAM)
                            .body(itemRepository.findById(id), Item.class);
                })
                .switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> addItem(ServerRequest serverRequest) {
        Mono<Item> itemMono = serverRequest.bodyToMono(Item.class);
        return itemMono.flatMap(item -> {
            item.setId(null);
            return ServerResponse
                .created(null)
                .contentType(ItemsRouter.JSON)
                .body(itemRepository.save(item), Item.class);
        });
    }

    public Mono<ServerResponse> deleteItem(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return ServerResponse
                    .ok()
                    .contentType(ItemsRouter.JSON)
                    .body(itemRepository.deleteById(id), Void.class);
    }

    public Mono<ServerResponse> updateItem(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Item> itemMono = serverRequest.bodyToMono(Item.class);;
        Mono<Item> existingItemMono = itemRepository.findById(id);
        return existingItemMono
                .flatMap(existingItem -> {
                    return itemMono.flatMap(item -> {
                        item.setId(id);
                        return ServerResponse
                                .ok()
                                .contentType(ItemsRouter.JSON)
                                .body(itemRepository.save(item), Item.class);
                    });

                })
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
