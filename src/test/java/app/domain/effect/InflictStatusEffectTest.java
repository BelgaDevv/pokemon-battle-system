package app.domain.effect;

import app.domain.enums.StatusType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InflictStatusEffectTest {

  // Verifies effect creation and correct status effect properties assignment
  @Test
  void shouldCreateEffectWithValidStatusEffect() {
    BurnEffect burnEffect = new BurnEffect();

    InflictStatusEffect effect =
        new InflictStatusEffect(burnEffect);

    assertNotNull(effect.getStatusEffect());
    assertEquals(
        StatusType.BURN,
        effect.getStatusEffect().getType()
    );
  }

  // Ensures the exact status effect instance reference is safely maintained
  @Test
  void shouldPreserveOriginalStatusEffectReference() {
    PoisonEffect poisonEffect = new PoisonEffect();

    InflictStatusEffect effect =
        new InflictStatusEffect(poisonEffect);

    assertSame(poisonEffect, effect.getStatusEffect());
  }

  // Confirms that passing a null status effect correctly triggers an exception
  @Test
  void shouldRejectNullStatusEffect() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new InflictStatusEffect(null)
    );
  }
}