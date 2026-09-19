package app.pokemon;

import app.attack.Attack;
import app.type.Type;

/**
 * Represents a Pokemon with its attributes, type, status, and attacks.
 */
public class Pokemon {

  private double hpMax;
  private double hpCurrent;
  private double spe;
  private double def;
  private double atk;
  private Status status;

  private Type type;
  private Attack[] attack;

  /**
   * Creates a Pokemon with the specified attributes and attacks.
   */
  public Pokemon(double atk, double def, double hpCurrent, double hpMax,
                 double spe, Type type, Attack[] attack) {

    if (hpMax <= 0) {
      throw new IllegalArgumentException("HP max must be greater than zero.");
    }

    if (hpCurrent < 0 || hpCurrent > hpMax) {
      throw new IllegalArgumentException(
          "Current HP must be between zero and max HP.");
    }

    if (atk < 0 || def < 0 || spe < 0) {
      throw new IllegalArgumentException(
          "ATK, DEF and SPE cannot be negative.");
    }

    validateAttacks(type, attack);

    this.atk = atk;
    this.def = def;
    this.hpCurrent = hpCurrent;
    this.hpMax = hpMax;
    this.spe = spe;
    this.type = type;
    this.status = null;
    this.attack = attack;
  }

  /**
   * Validates the attacks assigned to a Pokemon.
   */
  private void validateAttacks(Type pokemonType, Attack[] attacks) {

    if (attacks == null || attacks.length != 4) {
      throw new IllegalArgumentException(
          "A Pokemon must have exactly 4 attacks.");
    }

    for (Attack attack : attacks) {

      if (attack == null) {
        throw new IllegalArgumentException(
            "Pokemon cannot have a null attack.");
      }

      if (pokemonType.isWeakAgainst(attack.getType())) {
        throw new IllegalArgumentException(
            "Pokemon cannot have an attack of its weakness type.");
      }
    }
  }

  /**
   * Returns the attacks assigned to this Pokemon.
   */
  public Attack[] getAttack() {
    return attack;
  }

  /**
   * Sets the attacks assigned to this Pokemon.
   */
  public void setAttack(Attack[] attack) {
    validateAttacks(this.type, attack);
    this.attack = attack;
  }

  /**
   * Returns the current status of this Pokemon.
   */
  public Status getStatus() {
    return status;
  }

  /**
   * Sets the status of this Pokemon.
   */
  public void setStatus(Status status) {
    this.status = status;
  }

  /**
   * Returns the attack stat of this Pokemon.
   */
  public double getAtk() {
    return atk;
  }

  /**
   * Sets the attack stat of this Pokemon.
   */
  public void setAtk(double atk) {
    this.atk = atk;
  }

  /**
   * Returns the defense stat of this Pokemon.
   */
  public double getDef() {
    return def;
  }

  /**
   * Sets the defense stat of this Pokemon.
   */
  public void setDef(double def) {
    this.def = def;
  }

  /**
   * Returns the current HP of this Pokemon.
   */
  public double getHpCurrent() {
    return hpCurrent;
  }

  /**
   * Sets the current HP of this Pokemon.
   */
  public void setHpCurrent(double hpCurrent) {
    if (hpCurrent < 0) {
      this.hpCurrent = 0;
      return;
    }

    this.hpCurrent = Math.min(hpCurrent, hpMax);
  }

  /**
   * Returns the maximum HP of this Pokemon.
   */
  public double getHpMax() {
    return hpMax;
  }

  /**
   * Sets the maximum HP of this Pokemon.
   */
  public void setHpMax(double hpMax) {
    if (hpMax <= 0 || hpMax < hpCurrent) {
      throw new IllegalArgumentException(
          "HP max must be greater than zero and current HP.");
    }

    this.hpMax = hpMax;
  }

  /**
   * Returns the speed stat of this Pokemon.
   */
  public double getSpe() {
    return spe;
  }

  /**
   * Sets the speed stat of this Pokemon.
   */
  public void setSpe(double spe) {
    this.spe = spe;
  }

  /**
   * Returns the type of this Pokemon.
   */
  public Type getType() {
    return type;
  }

  /**
   * Sets the type of this Pokemon.
   */
  public void setType(Type type) {
    validateAttacks(type, this.attack);
    this.type = type;
  }
}