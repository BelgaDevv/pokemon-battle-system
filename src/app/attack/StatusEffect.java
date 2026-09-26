package app.attack;

import app.effect.Effect;
import app.pokemon.Pokemon;

/**
 * Represents effects that apply status conditions to a Pokemon.
 */
public class StatusEffect implements Effect {

  /**
   * Applies burn to the specified Pokemon, reducing its attack by 50 percent.
   */
  public void applyBurn(Pokemon pokemon) {
    if (pokemon == null) {
      throw new IllegalArgumentException("Pokemon cannot be null.");
    }

    pokemon.setAtk(pokemon.getAtk() * 0.50);
  }

  /**
   * Processes burn damage based on the Pokemon's maximum HP.
   */
  public void processBurn(Pokemon pokemon) {

    double damage = pokemon.getHpMax() * 0.06;
    pokemon.setHpCurrent(pokemon.getHpCurrent() - damage);
  }

  /**
   * Processes poison damage based on the Pokemon's maximum HP.
   */
  public void processPoison(Pokemon pokemon) {

    double damage = pokemon.getHpMax() * 0.04;
    pokemon.setHpCurrent(pokemon.getHpCurrent() - damage);
  }

  /**
   * Applies paralysis to the specified Pokemon, reducing its speed by 50 percent.
   */
  public void applyParalysis(Pokemon pokemon) {

    pokemon.setSpe(pokemon.getSpe() * 0.50);
  }
}