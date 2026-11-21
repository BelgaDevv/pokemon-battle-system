
package app.domain.service;

import app.domain.enums.ElementalType;
import app.domain.model.Attack;
import app.domain.model.Pokemon;
import app.domain.terrain.TerrainEffect;

import java.util.Objects;

/**
 * Calculates attack damage using Pokemon attributes,
 * elemental effectiveness, terrain effects and critical hits.
 */
public class DamageCalculator {

  private static final double CRITICAL_MULTIPLIER = 1.5;
  private static final double MINIMUM_DAMAGE = 1.0;

  /**
   * Calculates the final damage of an attack using effective
   * Attack and Defense, type effectiveness, terrain bonuses
   * and critical hits.
   * Rounds the result and ensures at least one damage point.
   */
  public int calculateDamage(
      Attack attack,
      Pokemon attacker,
      Pokemon defender,
      double typeMultiplier,
      TerrainEffect terrainEffect,
      boolean critical
  ) {
    Objects.requireNonNull(attack, "Attack cannot be null.");
    Objects.requireNonNull(attacker, "Attacker cannot be null.");
    Objects.requireNonNull(defender, "Defender cannot be null.");
    Objects.requireNonNull(terrainEffect, "Terrain effect cannot be null.");

    if (typeMultiplier <= 0) {
      throw new IllegalArgumentException(
          "Type multiplier must be greater than zero."
      );
    }

    double effectiveAttack = attacker.getEffectiveAttack();
    double effectiveDefense = defender.getEffectiveDefense();

    double terrainMultiplier = getTerrainMultiplier(
        attacker.getType(),
        terrainEffect
    );

    double criticalMultiplier = critical
        ? CRITICAL_MULTIPLIER
        : 1.0;

    double damage = attack.getPower()
        * (effectiveAttack / effectiveDefense)
        * typeMultiplier
        * terrainMultiplier
        * criticalMultiplier;

    return Math.max(
        (int) MINIMUM_DAMAGE,
        (int) Math.round(damage)
    );
  }

  /**
   * Returns the terrain damage multiplier based on
   * the attacking Pokemon's elemental type.
   * Grass attacks use a neutral multiplier.
   */
  private double getTerrainMultiplier(
      ElementalType attackerType,
      TerrainEffect terrainEffect
  ) {
    return switch (attackerType) {
      case FIRE -> terrainEffect.getFireDamageMultiplier();
      case WATER -> terrainEffect.getWaterDamageMultiplier();
      case GRASS -> 1.0;
    };
  }
}
