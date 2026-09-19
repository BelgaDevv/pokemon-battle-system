package app.trainer;

import app.pokemon.Pokemon;

/*
store trainer info
 */
public class Trainer {

    private String name;
    private Pokemon pokemon;
    private int itemCount;

    // class constructor
    public Trainer(int itemCount, String name, Pokemon pokemon) {
        this.itemCount = itemCount;
        this.name = name;
        this.pokemon = pokemon;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
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
}

