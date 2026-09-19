package app.pokemon;

import app.attack.StatusEffect;

/**
 * Represents a status condition that can affect a Pokemon.
 */
public class Status {

  private String name;
  private StatusEffect statusEffect;

  /**
   * Creates a status with the specified name and effect.
   */
  public Status(String name, StatusEffect statusEffect) {
    this.name = name;
    this.statusEffect = statusEffect;
  }

  /**
   * Returns the name of this status.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the effect associated with this status.
   */
  public StatusEffect getStatusEffect() {
    return statusEffect;
  }
}