package app.item;


public class Item {

    private String name;
    private ItemEffect itemEffect;

    // class constructor
    public Item(ItemEffect itemEffect, String name) {
        this.itemEffect = itemEffect;
        this.name = name;
    }

    public ItemEffect getItemEffect() {
        return itemEffect;
    }

    public String getName() {
        return name;
    }
}
