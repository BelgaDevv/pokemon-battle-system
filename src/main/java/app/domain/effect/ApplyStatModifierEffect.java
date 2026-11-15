package app.domain.effect;

/**
 * Applies a stat modifier to a Pokemon when an attack effect
 * is executed, changing one of its effective battle attributes.
 */
public class ApplyStatModifierEffect implements AttackEffect {

  private final StatModifier statModifier;

  public ApplyStatModifierEffect(StatModifier statModifier) {
    if (statModifier == null) {
      throw new IllegalArgumentException(
          "Stat modifier cannot be null."
      );
    }

    this.statModifier = statModifier;
  }

  public StatModifier getStatModifier() {
    return statModifier;
  }
}