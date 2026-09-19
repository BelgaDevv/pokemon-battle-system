package app.trainer;

import app.item.Item;
import app.pokemon.Pokemon;

/**
 * Represents a trainer participating in a battle.
 */
public class Trainer {

  private String name;
  private Pokemon pokemon;
  private Item[] items;
  private int itemCount;

  /**
   * Creates a trainer with the specified name, Pokemon, and items.
   */
  public Trainer(String name, Pokemon pokemon, Item[] items) {
    this.name = name;
    this.pokemon = pokemon;
    this.items = items;
    this.itemCount = 0;
  }

  /**
   * Checks whether the trainer can use another item.
   */
  public boolean canUseItem() {
    return itemCount < 2;
  }

  /**
   * Uses an item if the trainer has not reached the item limit.
   */
  public boolean useItem() {
    if (!canUseItem()) {
      return false;
    }

    itemCount++;
    return true;
  }

  /**
   * Returns the number of items already used by the trainer.
   */
  public int getItemCount() {
    return itemCount;
  }

  /**
   * Returns the items available to the trainer.
   */
  public Item[] getItems() {
    return items;
  }

  /**
   * Returns the name of the trainer.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the trainer.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the Pokemon used by the trainer.
   */
  public Pokemon getPokemon() {
    return pokemon;
  }

  /**
   * Sets the Pokemon used by the trainer.
   */
  public void setPokemon(Pokemon pokemon) {
    this.pokemon = pokemon;
  }

  /**
   * Sets the items available to the trainer.
   */
  public void setItems(Item[] items) {
    this.items = items;
  }
}