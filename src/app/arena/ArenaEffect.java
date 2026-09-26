package app.arena;

import app.attack.Attack;
import app.effect.Effect;
import app.pokemon.Pokemon;
import app.type.Type;

/**
 * Represents an effect applied by a specific arena.
 */
public class ArenaEffect implements Effect {

  /**
   * Represents the types of effects that can be applied by an arena.
   */
  public enum EffectType {
    HOT_ASPHALT, RAIN_PUDDLE, CENTRAL_FLOWERBED
  }

  private EffectType effectType;

  /**
   * Creates an arena effect with the specified effect type.
   */
  public ArenaEffect(EffectType effectType) {
    this.effectType = effectType;
  }

  /**
   * Applies a fire damage bonus when the arena effect is hot asphalt.
   */
  public double fireDamageBonus(double damage, Attack attack) {
    if (effectType == EffectType.HOT_ASPHALT && attack.getType() == Type.FIRE) {
      return damage * 1.15;
    }

    return damage;
  }

  /**
   * Applies a water damage bonus when the arena effect is a rain puddle.
   */
  public double waterDamageBonus(double damage, Attack attack) {
    if (effectType == EffectType.RAIN_PUDDLE && attack.getType() == Type.WATER) {
      return damage * 1.10;
    }

    return damage;
  }

  /**
   * Applies a recovery bonus to a plant Pokemon in the central flowerbed.
   */
  public void plantRecoverBonus(Pokemon pokemon) {
    if (effectType == EffectType.CENTRAL_FLOWERBED && pokemon.getType() == Type.PLANT) {

      double recovery = pokemon.getHpMax() * 0.05;
      double newHp = pokemon.getHpCurrent() + recovery;

      pokemon.setHpCurrent(Math.min(newHp, pokemon.getHpMax()));
    }
  }

  /**
   * Returns the type of this arena effect.
   */
  public EffectType getEffectType() {
    return effectType;
  }
}