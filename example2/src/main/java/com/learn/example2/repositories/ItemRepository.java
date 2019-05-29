package com.learn.example2.repositories;

import com.learn.example2.models.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ItemRepository extends ReactiveMongoRepository<Item, String> {
    Flux<Item> findByDescription(String description);
}
