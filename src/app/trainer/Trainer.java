package app.trainer;

import app.item.Item;
import app.pokemon.Pokemon;

public class Trainer {

    private String name;
    private Pokemon pokemon;
    private Item[] items;
    private int itemCount;

    public Trainer(String name, Pokemon pokemon, Item[] items) {
        this.name = name;
        this.pokemon = pokemon;
        this.items = items;
        this.itemCount = 0;
    }

    public boolean canUseItem() {
        return itemCount < 2;
    }

    public boolean useItem() {
        if (!canUseItem()) {
            return false;
        }

        itemCount++;
        return true;
    }

    public int getItemCount() {
        return itemCount;
    }

    public Item[] getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }
}