package app.type;

/**
 * Represents the elemental types of Pokemon.
 */
public enum Type {

  PLANT,
  FIRE,
  WATER;

  /**
   * Returns the type that this type has an advantage against.
   */
  public Type getAdvantage() {
    return switch (this) {
      case PLANT -> WATER;
      case FIRE -> PLANT;
      case WATER -> FIRE;
    };
  }

  /**
   * Returns the type that this type has a disadvantage against.
   */
  public Type getDisadvantage() {
    return switch (this) {
      case PLANT -> FIRE;
      case FIRE -> WATER;
      case WATER -> PLANT;
    };
  }

  /**
   * Returns the damage multiplier against the specified defender type.
   */
  public double getDamageMultiplier(Type defenderType) {
    if (defenderType == getAdvantage()) {
      return 2.0;
    }

    if (defenderType == getDisadvantage()) {
      return 0.5;
    }

    return 1.0;
  }

  /**
   * Checks whether this type is weak against the specified attack type.
   */
  public boolean isWeakAgainst(Type attackType) {
    return attackType == getDisadvantage();
  }
}