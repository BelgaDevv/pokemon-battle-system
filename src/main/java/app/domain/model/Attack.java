package app.domain.model;

import app.domain.effect.AttackEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an attack that a Pokemon can use during a battle.
 *
 * Each attack has independent power, accuracy, PP values and
 * effects it can trigger.
 */
public class Attack {

  private final String name;
  private final int power;
  private final double accuracy;
  private final int maxPP;

  private int currentPP;

  private final List<AttackEffect> effects;

  public Attack(
      String name,
      int power,
      double accuracy,
      int maxPP,
      List<AttackEffect> effects
  ) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException(
          "Attack name cannot be null or blank."
      );
    }

    if (power <= 0) {
      throw new IllegalArgumentException(
          "Attack power must be greater than zero."
      );
    }

    if (accuracy < 0 || accuracy > 100) {
      throw new IllegalArgumentException(
          "Attack accuracy must be between 0 and 100."
      );
    }

    if (maxPP <= 0) {
      throw new IllegalArgumentException(
          "Maximum PP must be greater than zero."
      );
    }

    if (effects == null) {
      throw new IllegalArgumentException(
          "Attack effects cannot be null."
      );
    }

    this.name = name;
    this.power = power;
    this.accuracy = accuracy;
    this.maxPP = maxPP;
    this.currentPP = maxPP;
    this.effects = new ArrayList<>(effects);
  }

  public String getName() {
    return name;
  }

  public int getPower() {
    return power;
  }

  public double getAccuracy() {
    return accuracy;
  }

  public int getMaxPP() {
    return maxPP;
  }

  public int getCurrentPP() {
    return currentPP;
  }

  public List<AttackEffect> getEffects() {
    return Collections.unmodifiableList(effects);
  }

  public boolean canBeUsed() {
    return currentPP > 0;
  }

  public void consumePP() {
    if (!canBeUsed()) {
      throw new IllegalStateException(
          "Attack has no remaining PP."
      );
    }

    currentPP--;
  }

  public void restorePP() {
    currentPP = maxPP;
  }
}