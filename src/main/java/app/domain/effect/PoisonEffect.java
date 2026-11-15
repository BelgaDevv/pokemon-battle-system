package app.domain.effect;


import app.domain.enums.StatusType;

/**
 * Represents the poisoned status condition.
 *
 * Poison causes progressive health loss while active.
 */
public class PoisonEffect extends StatusEffect {

  public PoisonEffect() {
    super(StatusType.POISON);
  }
}