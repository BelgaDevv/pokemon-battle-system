package app.domain.effect;

/**
 * Applies a status condition to a Pokemon when the attack effect
 * is processed during battle.
 * <p>
 * Stores the status effect that will be applied to the target,
 * such as burn, poison or paralysis.
 */
public class InflictStatusEffect implements AttackEffect {

  private final StatusEffect statusEffect;

  public InflictStatusEffect(StatusEffect statusEffect) {
    if (statusEffect == null) {
      throw new IllegalArgumentException(
          "Status effect cannot be null."
      );
    }

    this.statusEffect = statusEffect;
  }

  public StatusEffect getStatusEffect() {
    return statusEffect;
  }
}