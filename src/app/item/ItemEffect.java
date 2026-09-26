package app.item;

import app.effect.Effect;
import app.pokemon.Pokemon;

/**
 * Represents effects that can be applied by an item.
 */
public class ItemEffect implements Effect {

  /**
   * Restores 20 HP to the specified Pokemon without exceeding its maximum HP.
   */
  public void heal(Pokemon pokemon) {
    if (pokemon == null) {
      throw new IllegalArgumentException("Pokemon cannot be null.");
    }

    double newHp = pokemon.getHpCurrent() + 20;

    if (newHp > pokemon.getHpMax()) {
      newHp = pokemon.getHpMax();
    }

    pokemon.setHpCurrent(newHp);
  }

  /**
   * Removes the burn status from the specified Pokemon.
   */
  public void clearBurn(Pokemon pokemon) {
    if (pokemon == null) {
      throw new IllegalArgumentException("Pokemon cannot be null.");
    }

    if (pokemon.getStatus() != null
        && "Burn".equals(pokemon.getStatus().getName())) {
      pokemon.setStatus(null);
    }
  }

  /**
   * Removes the poison status from the specified Pokemon.
   */
  public void clearPoison(Pokemon pokemon) {
    if (pokemon == null) {
      throw new IllegalArgumentException("Pokemon cannot be null.");
    }

    if (pokemon.getStatus() != null
        && "Poison".equals(pokemon.getStatus().getName())) {
      pokemon.setStatus(null);
    }
  }

  /**
   * Removes the paralysis status from the specified Pokemon.
   */
  public void clearParalysis(Pokemon pokemon) {
    if (pokemon == null) {
      throw new IllegalArgumentException("Pokemon cannot be null.");
    }

    if (pokemon.getStatus() != null
        && "Paralysis".equals(pokemon.getStatus().getName())) {
      pokemon.setStatus(null);
    }
  }
}