package app.domain.effect;


import app.domain.enums.StatType;

/**
 * Represents a temporary modification applied to a Pokemon's base stats.
 *
 * The modifier uses a multiplier to calculate the effective attribute value.
 */
public class StatModifier {

  private final StatType statType;
  private final double multiplier;

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

  public StatType getStatType() {
    return statType;
  }

  public double getMultiplier() {
    return multiplier;
  }
}