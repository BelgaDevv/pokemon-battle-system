package app.domain.model;

import java.util.Objects;

/**
 * Represents a trainer participating in a Pokemon battle.
 *
 * A trainer owns one Pokemon and can use a limited number
 * of items during each battle.
 */
public class Trainer {

  private final String name;
  private Pokemon pokemon;
  private int itemsUsed;

  private static final int MAX_ITEMS_PER_BATTLE = 2;

  public Trainer(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException(
          "Trainer name cannot be null or blank."
      );
    }

    this.name = name;
    this.itemsUsed = 0;
  }

  public String getName() {
    return name;
  }

  public Pokemon getPokemon() {
    return pokemon;
  }

  public void setPokemon(Pokemon pokemon) {
    this.pokemon = Objects.requireNonNull(
        pokemon,
        "Pokemon cannot be null."
    );
  }

  public int getItemsUsed() {
    return itemsUsed;
  }

  public boolean canUseItem() {
    return itemsUsed < MAX_ITEMS_PER_BATTLE;
  }

  public void registerItemUse() {
    if (!canUseItem()) {
      throw new IllegalStateException(
          "Trainer has reached the item usage limit."
      );
    }

    itemsUsed++;
  }

  public void resetItemsUsed() {
    itemsUsed = 0;
  }
}