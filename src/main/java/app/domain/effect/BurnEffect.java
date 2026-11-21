package app.domain.effect;


import app.domain.enums.StatusType;

/**
 * Represents the burned status condition.
 * <p>
 * Burn reduces the affected Pokemon's health and attack power.
 */
public class BurnEffect extends StatusEffect {

  public BurnEffect() {
    super(StatusType.BURN);
  }
}