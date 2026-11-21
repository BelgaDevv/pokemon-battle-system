
package app.domain.terrain;

/**
 * Stores terrain bonuses that affect attack damage,
 * accuracy and healing during battles.
 */
public class TerrainEffect {

  private final double fireDamageMultiplier;
  private final double waterDamageMultiplier;
  private final double waterAccuracyBonus;
  private final double grassHealingPercentage;

  /**
   * Creates a terrain effect with the specified combat bonuses.
   * Damage multipliers must be positive, while accuracy
   * and healing bonuses cannot be negative.
   */
  public TerrainEffect(
      double fireDamageMultiplier,
      double waterDamageMultiplier,
      double waterAccuracyBonus,
      double grassHealingPercentage
  ) {
    if (fireDamageMultiplier <= 0
        || waterDamageMultiplier <= 0
        || waterAccuracyBonus < 0
        || grassHealingPercentage < 0) {
      throw new IllegalArgumentException(
          "Terrain effect values are invalid."
      );
    }

    this.fireDamageMultiplier = fireDamageMultiplier;
    this.waterDamageMultiplier = waterDamageMultiplier;
    this.waterAccuracyBonus = waterAccuracyBonus;
    this.grassHealingPercentage = grassHealingPercentage;
  }


  public double getFireDamageMultiplier() {
    return fireDamageMultiplier;
  }


  public double getWaterDamageMultiplier() {
    return waterDamageMultiplier;
  }


  public double getWaterAccuracyBonus() {
    return waterAccuracyBonus;
  }


  public double getGrassHealingPercentage() {
    return grassHealingPercentage;
  }
}
