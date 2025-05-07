package io.github.mufca.libgdx.datastructure.inventory;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    // Inventory – maintained as three separate lists
    private List<Item> inventory;       // All items
    private List<Item> equippedItems;   // Currently equipped items
    private List<Item> visibleItems;    // Items visible on the character

    public Inventory() {
        inventory = new ArrayList<>();
        equippedItems = new ArrayList<>();
        visibleItems = new ArrayList<>();
    }
}
