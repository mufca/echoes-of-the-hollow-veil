package io.github.mufca.libgdx.datastructure.inventory;

import lombok.Data;

@Data
public class Item {

    private final ItemBase itemBase;
    private float value;
    private float weight;
    private float durability;


    public Item(ItemBase itemBase, float value, float weight, float durability) {
        this.itemBase = itemBase;
        this.value = value;
        this.weight = weight;
        this.durability = durability;
    }

    public Item(ItemBase itemBase) {
        this(itemBase,0f,0f,100f);
    }
}
