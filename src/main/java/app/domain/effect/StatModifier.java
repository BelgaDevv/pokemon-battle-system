
package app.domain.effect;

import app.domain.enums.StatType;

/**
 * Defines a temporary multiplier applied to
 * a Pokemon's Attack, Defense or Speed.
 */
public class StatModifier {

  private final StatType statType;
  private final double multiplier;

  /**
   * Creates a stat modifier for the specified attribute.
   * The multiplier must be greater than zero.
   */
  public StatModifier(StatType statType, double multiplier) {
    if (statType == null) {
      throw new IllegalArgumentException(
          "Stat type cannot be null."
      );
    }

    if (multiplier <= 0) {
      throw new IllegalArgumentException(
          "Multiplier must be greater than zero."
      );
    }

    this.statType = statType;
    this.multiplier = multiplier;
  }

  /**
   * Returns the attribute affected by the modifier.
   */
  public StatType getStatType() {
    return statType;
  }

  /**
   * Returns the multiplier used to calculate
   * the effective attribute value.
   */
  public double getMultiplier() {
    return multiplier;
  }
}
