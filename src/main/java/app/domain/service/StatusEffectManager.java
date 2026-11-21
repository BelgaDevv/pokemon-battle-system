
package app.domain.service;

import app.domain.effect.StatModifier;
import app.domain.effect.StatusEffect;
import app.domain.enums.StatType;
import app.domain.enums.StatusType;
import app.domain.model.Pokemon;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Applies status conditions and processes their effects
 * on Pokemon during battles.
 *
 * Handles stat penalties from burn and paralysis,
 * as well as damage from burn and progressive poison.
 */
public class StatusEffectManager {

  private static final double BURN_DAMAGE_PERCENTAGE = 0.05;
  private static final double INITIAL_POISON_DAMAGE_PERCENTAGE = 0.05;
  private static final double POISON_DAMAGE_INCREMENT = 0.02;
  private static final double MAX_POISON_DAMAGE_PERCENTAGE = 0.15;

  private static final double BURN_ATTACK_MULTIPLIER = 0.80;
  private static final double PARALYSIS_SPEED_MULTIPLIER = 0.50;

  private final Map<Pokemon, Integer> poisonTurnCounts = new IdentityHashMap<>();

  /**
   * Applies a status condition to a living Pokemon.
   * Burn reduces Attack, paralysis reduces Speed,
   * and poison initializes progressive damage tracking.
   * Existing conditions of the same type are not reapplied.
   */
  public void applyStatusEffect(Pokemon pokemon, StatusEffect statusEffect) {
    Objects.requireNonNull(pokemon, "Pokemon cannot be null.");
    Objects.requireNonNull(statusEffect, "Status effect cannot be null.");

    if (!pokemon.isAlive()) {
      return;
    }

    if (pokemon.hasStatusEffect(statusEffect.getType())) {
      return;
    }

    pokemon.addStatusEffect(statusEffect);

    switch (statusEffect.getType()) {
      case BURN -> addStatModifier(
          pokemon,
          StatType.ATTACK,
          BURN_ATTACK_MULTIPLIER
      );

      case PARALYSIS -> addStatModifier(
          pokemon,
          StatType.SPEED,
          PARALYSIS_SPEED_MULTIPLIER
      );

      case POISON -> poisonTurnCounts.put(pokemon, 0);
    }
  }

  /**
   * Processes burn and poison damage at the end of a turn.
   * Poison damage increases progressively up to its limit.
   * Stops processing if the Pokemon faints.
   */
  public void processEndOfTurn(Pokemon pokemon) {
    Objects.requireNonNull(pokemon, "Pokemon cannot be null.");

    if (!pokemon.isAlive()) {
      poisonTurnCounts.remove(pokemon);
      return;
    }

    if (pokemon.hasStatusEffect(StatusType.BURN)) {
      int burnDamage = calculatePercentageDamage(
          pokemon,
          BURN_DAMAGE_PERCENTAGE
      );

      pokemon.takeDamage(burnDamage);
    }

    if (!pokemon.isAlive()) {
      poisonTurnCounts.remove(pokemon);
      return;
    }

    if (pokemon.hasStatusEffect(StatusType.POISON)) {
      int poisonTurns = poisonTurnCounts.getOrDefault(pokemon, 0) + 1;

      poisonTurnCounts.put(pokemon, poisonTurns);

      double poisonPercentage = Math.min(
          INITIAL_POISON_DAMAGE_PERCENTAGE
              + (poisonTurns - 1) * POISON_DAMAGE_INCREMENT,
          MAX_POISON_DAMAGE_PERCENTAGE
      );

      int poisonDamage = calculatePercentageDamage(
          pokemon,
          poisonPercentage
      );

      pokemon.takeDamage(poisonDamage);
    } else {
      poisonTurnCounts.remove(pokemon);
    }

    if (!pokemon.isAlive()) {
      poisonTurnCounts.remove(pokemon);
    }
  }

  /**
   * Adds a stat modifier if the Pokemon does not
   * already have a modifier for the same stat.
   */
  private void addStatModifier(
      Pokemon pokemon,
      StatType statType,
      double multiplier
  ) {
    boolean alreadyApplied = pokemon.getStatModifiers()
        .stream()
        .anyMatch(modifier -> modifier.getStatType() == statType);

    if (!alreadyApplied) {
      pokemon.addStatModifier(new StatModifier(statType, multiplier));
    }
  }

  /**
   * Calculates damage as a percentage of maximum HP,
   * rounding up and ensuring at least one damage point.
   */
  private int calculatePercentageDamage(
      Pokemon pokemon,
      double percentage
  ) {
    return Math.max(
        1,
        (int) Math.ceil(pokemon.getMaxHp() * percentage)
    );
  }
}
