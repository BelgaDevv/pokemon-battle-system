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
 * Applies and processes status conditions during Pokemon battles.
 *
 * Handles burn and poison damage at the end of each turn,
 * applies stat penalties for burn and paralysis, and tracks
 * progressive poison damage for each affected Pokemon.
 */
public class StatusEffectManager {

  private static final double BURN_DAMAGE_PERCENTAGE = 0.05;
  private static final double INITIAL_POISON_DAMAGE_PERCENTAGE = 0.05;
  private static final double POISON_DAMAGE_INCREMENT = 0.02;
  private static final double MAX_POISON_DAMAGE_PERCENTAGE = 0.15;

  private static final double BURN_ATTACK_MULTIPLIER = 0.80;
  private static final double PARALYSIS_SPEED_MULTIPLIER = 0.50;

  private final Map<Pokemon, Integer> poisonTurnCounts = new IdentityHashMap<>();


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