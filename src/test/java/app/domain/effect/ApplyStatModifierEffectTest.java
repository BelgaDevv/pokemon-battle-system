package app.domain.effect;

import app.domain.enums.StatType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplyStatModifierEffectTest {

  // Verifies effect creation and correct stat modifier properties assignment
  @Test
  void shouldCreateEffectWithValidStatModifier() {
    StatModifier modifier =
        new StatModifier(StatType.ATTACK, 1.5);

    ApplyStatModifierEffect effect =
        new ApplyStatModifierEffect(modifier);

    assertNotNull(effect.getStatModifier());
    assertEquals(
        StatType.ATTACK,
        effect.getStatModifier().getStatType()
    );
    assertEquals(
        1.5,
        effect.getStatModifier().getMultiplier()
    );
  }

  // Ensures the exact modifier instance reference is safely maintained
  @Test
  void shouldPreserveOriginalStatModifierReference() {
    StatModifier modifier =
        new StatModifier(StatType.DEFENSE, 0.5);

    ApplyStatModifierEffect effect =
        new ApplyStatModifierEffect(modifier);

    assertSame(modifier, effect.getStatModifier());
  }

  // Confirms that passing a null modifier correctly triggers an exception
  @Test
  void shouldRejectNullStatModifier() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new ApplyStatModifierEffect(null)
    );
  }
}