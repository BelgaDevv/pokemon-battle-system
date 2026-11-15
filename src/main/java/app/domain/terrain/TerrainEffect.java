package app.domain.terrain;

/**
 * Represents the combat bonuses provided by a terrain.
 */
public class TerrainEffect {

  private final double fireDamageMultiplier;
  private final double waterDamageMultiplier;
  private final double waterAccuracyBonus;
  private final double grassHealingPercentage;

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