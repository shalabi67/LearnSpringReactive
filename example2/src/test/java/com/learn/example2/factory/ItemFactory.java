package com.learn.example2.factory;

import com.learn.example2.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {
    public static List<Item> createItems(int count) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Item item = new Item(null, "description" + i, 1.0 * i + .59);
            items.add(item);
        }

        return items;
    }

    public static List<Item> createItemsWithCommonDescription(int count) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Item item = new Item(null, "description", 1.0 * i + .59);
            items.add(item);
        }

        return items;
    }

    public static Item create() {
        return new Item("aaa", "description aaa", 99.59);
    }
}
