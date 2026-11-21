
package app.domain.model;

import app.domain.effect.AttackEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Defines a Pokemon attack, including its damage power,
 * accuracy, PP limits and additional effects.
 */
public class Attack {

  private final String name;
  private final int power;
  private final double accuracy;
  private final int maxPP;
  private final List<AttackEffect> effects;
  private int currentPP;

  /**
   * Creates an attack with its combat attributes and effects.
   * Initializes the current PP to the maximum value.
   */
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

  /**
   * Returns the attack name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the base power of the attack.
   */
  public int getPower() {
    return power;
  }

  /**
   * Returns the attack accuracy on a scale from 0 to 100.
   */
  public double getAccuracy() {
    return accuracy;
  }

  /**
   * Returns the maximum number of PP available.
   */
  public int getMaxPP() {
    return maxPP;
  }

  /**
   * Returns the remaining PP.
   */
  public int getCurrentPP() {
    return currentPP;
  }

  /**
   * Returns the attack's additional effects
   * as an unmodifiable list.
   */
  public List<AttackEffect> getEffects() {
    return Collections.unmodifiableList(effects);
  }

  /**
   * Checks whether the attack has enough PP to be used.
   */
  public boolean canBeUsed() {
    return currentPP > 0;
  }

  /**
   * Consumes one PP when the attack is used.
   * Throws an exception if no PP remains.
   */
  public void consumePP() {
    if (!canBeUsed()) {
      throw new IllegalStateException(
          "Attack has no remaining PP."
      );
    }

    currentPP--;
  }

  /**
   * Restores the attack's PP to its maximum value.
   */
  public void restorePP() {
    currentPP = maxPP;
  }
}
