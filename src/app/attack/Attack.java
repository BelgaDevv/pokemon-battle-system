package app.attack;

import app.pokemon.Status;
import app.type.Type;

/**
 * Represents an attack that can be used by a Pokemon.
 */
public class Attack {

  private String name;
  private int ppMax;
  private int ppCurrent;
  private double damage;
  private Type type;
  private Status status;
  private AttackEffect attackEffect;

  /**
   * Creates an attack with the specified properties.
   */
  public Attack(AttackEffect attackEffect, double damage, String name, int ppCurrent,
                int ppMax, Status status, Type type) {

    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Attack name cannot be empty.");
    }

    if (ppMax <= 0) {
      throw new IllegalArgumentException("PP max must be greater than zero.");
    }

    if (ppCurrent < 0 || ppCurrent > ppMax) {
      throw new IllegalArgumentException(
          "Current PP must be between zero and max PP.");
    }

    if (damage < 0) {
      throw new IllegalArgumentException(
          "Damage cannot be negative.");
    }

    if (type == null) {
      throw new IllegalArgumentException(
          "Attack type cannot be null.");
    }

    this.attackEffect = attackEffect;
    this.damage = damage;
    this.name = name;
    this.ppCurrent = ppCurrent;
    this.ppMax = ppMax;
    this.status = status;
    this.type = type;
  }

  /**
   * Checks whether the attack can currently be used.
   */
  public boolean canUse() {
    return ppCurrent > 0;
  }

  /**
   * Uses the attack and decreases its current PP by one.
   */
  public boolean use() {
    if (!canUse()) {
      return false;
    }

    ppCurrent--;
    return true;
  }

  /**
   * Returns the effect associated with this attack.
   */
  public AttackEffect getAttackEffect() {
    return attackEffect;
  }

  /**
   * Sets the effect associated with this attack.
   */
  public void setAttackEffect(AttackEffect attackEffect) {
    this.attackEffect = attackEffect;
  }

  /**
   * Returns the damage dealt by this attack.
   */
  public double getDamage() {
    return damage;
  }

  /**
   * Returns the name of this attack.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the current number of power points.
   */
  public int getPpCurrent() {
    return ppCurrent;
  }

  /**
   * Returns the maximum number of power points.
   */
  public int getPpMax() {
    return ppMax;
  }

  /**
   * Returns the status effect associated with this attack.
   */
  public Status getStatus() {
    return status;
  }

  /**
   * Sets the status effect associated with this attack.
   */
  public void setStatus(Status status) {
    this.status = status;
  }

  /**
   * Returns the type of this attack.
   */
  public Type getType() {
    return type;
  }
}