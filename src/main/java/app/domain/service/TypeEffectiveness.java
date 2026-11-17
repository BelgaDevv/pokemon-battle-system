package app.domain.service;

import app.domain.enums.ElementalType;

import java.util.Objects;

/**
 * Calculates type effectiveness multipliers for Pokemon attacks.
 *
 * Determines whether an attack deals increased, reduced or
 * normal damage based on the attack and target elemental types.
 */
public class TypeEffectiveness {


  public double calculateMultiplier(
      ElementalType attackType,
      ElementalType defenderType
  ) {
    Objects.requireNonNull(attackType, "Attack type cannot be null.");
    Objects.requireNonNull(defenderType, "Defender type cannot be null.");

    if (attackType == defenderType) {
      return 1.0;
    }

    return switch (attackType) {
      case FIRE -> defenderType == ElementalType.GRASS ? 2.0 : 0.5;
      case WATER -> defenderType == ElementalType.FIRE ? 2.0 : 0.5;
      case GRASS -> defenderType == ElementalType.WATER ? 2.0 : 0.5;
    };
  }
}