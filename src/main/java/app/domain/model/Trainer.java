
package app.domain.model;

import java.util.Objects;

/**
 * Manages a trainer's Pokemon and item usage
 * during battles.
 */
public class Trainer {

  private static final int MAX_ITEMS_PER_BATTLE = 2;

  private final String name;
  private Pokemon pokemon;
  private int itemsUsed;

  /**
   * Creates a trainer with the specified name
   * and initializes the item usage counter.
   */
  public Trainer(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException(
          "Trainer name cannot be null or blank."
      );
    }

    this.name = name;
    this.itemsUsed = 0;
  }

  /**
   * Returns the trainer's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the Pokemon assigned to the trainer,
   * or null if none has been assigned.
   */
  public Pokemon getPokemon() {
    return pokemon;
  }

  /**
   * Assigns a Pokemon to the trainer.
   * The Pokemon cannot be null.
   */
  public void setPokemon(Pokemon pokemon) {
    this.pokemon = Objects.requireNonNull(
        pokemon,
        "Pokemon cannot be null."
    );
  }

  /**
   * Returns the number of items used
   * since the last reset.
   */
  public int getItemsUsed() {
    return itemsUsed;
  }

  /**
   * Checks whether the trainer can use another item
   * without exceeding the battle limit.
   */
  public boolean canUseItem() {
    return itemsUsed < MAX_ITEMS_PER_BATTLE;
  }

  /**
   * Registers one item use.
   * Throws an exception if the usage limit has been reached.
   */
  public void registerItemUse() {
    if (!canUseItem()) {
      throw new IllegalStateException(
          "Trainer has reached the item usage limit."
      );
    }

    itemsUsed++;
  }

  /**
   * Resets the item usage counter to zero
   * for a new battle.
   */
  public void resetItemsUsed() {
    itemsUsed = 0;
  }
}
