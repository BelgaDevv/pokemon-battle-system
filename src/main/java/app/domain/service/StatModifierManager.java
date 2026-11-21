package app.domain.service;

import app.domain.effect.StatModifier;
import app.domain.enums.StatType;
import app.domain.model.Pokemon;

import java.util.Objects;

/**
 * Manages stat modifiers applied to Pokemon during battles.
 * <p>
 * Delegates modifier storage to the Pokemon model and provides
 * operations to apply and remove temporary attribute changes.
 */
public class StatModifierManager {


  public void applyModifier(Pokemon pokemon, StatModifier modifier) {
    Objects.requireNonNull(pokemon, "Pokemon cannot be null.");
    Objects.requireNonNull(modifier, "Stat modifier cannot be null.");

    pokemon.addStatModifier(modifier);
  }


  public void removeModifiers(Pokemon pokemon, StatType statType) {
    Objects.requireNonNull(pokemon, "Pokemon cannot be null.");
    Objects.requireNonNull(statType, "Stat type cannot be null.");

    pokemon.removeStatModifiers(statType);
  }
}