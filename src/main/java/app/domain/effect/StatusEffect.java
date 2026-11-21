package app.domain.effect;


import app.domain.enums.StatusType;

/**
 * Represents a negative status condition applied to a Pokemon.
 * <p>
 * Status effects remain active until the affected Pokemon faints
 * or an appropriate item removes the condition.
 */
public abstract class StatusEffect {

  private final StatusType type;

  protected StatusEffect(StatusType type) {
    this.type = type;
  }

  public StatusType getType() {
    return type;
  }
}