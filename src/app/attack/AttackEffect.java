package app.attack;

import app.effect.Effect;
import app.pokemon.Pokemon;

/**
 * Represents an effect that can modify a Pokemon's attributes.
 */
public class AttackEffect implements Effect {

  /**
   * Increases the Pokemon's speed by 10 percent.
   */
  public void increaseSpeed(Pokemon pokemon) {
    pokemon.setSpe(pokemon.getSpe() * 1.10);
  }

  /**
   * Increases the Pokemon's attack by 10 percent.
   */
  public void increaseAttack(Pokemon pokemon) {
    pokemon.setAtk(pokemon.getAtk() * 1.10);
  }

  /**
   * Increases the Pokemon's defense by 10 percent.
   */
  public void increaseDefense(Pokemon pokemon) {

    pokemon.setDef(pokemon.getDef() * 1.10);
  }
}