package app.domain.effect;


import app.domain.enums.StatusType;

/**
 * Represents the paralyzed status condition.
 *
 * Paralysis reduces the affected Pokemon's speed.
 */
public class ParalysisEffect extends StatusEffect {

  public ParalysisEffect() {
    super(StatusType.PARALYSIS);
  }
}