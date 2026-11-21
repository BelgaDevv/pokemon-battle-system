
package app.domain.service;

import app.domain.enums.ElementalType;

import java.util.Objects;

/**
 * Calculates attack damage multipliers based on
 * the elemental types of the attack and defender.
 */
public class TypeEffectiveness {

  /**
   * Calculates elemental effectiveness using Fire, Water
   * and Grass type advantages.
   * Returns 2.0 for effective attacks, 0.5 for resisted
   * attacks and 1.0 for attacks of the same type.
   */
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
