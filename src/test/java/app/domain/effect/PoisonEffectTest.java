package app.domain.effect;

import app.domain.enums.StatusType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class PoisonEffectTest {

  // Verifies that the effect correctly exposes the POISON status type
  @Test
  void shouldHavePoisonStatusType() {
    PoisonEffect effect = new PoisonEffect();

    assertEquals(StatusType.POISON, effect.getType());
  }

  // Ensures separate effect instances are independently allocated
  @Test
  void shouldCreateIndependentInstances() {
    PoisonEffect first = new PoisonEffect();
    PoisonEffect second = new PoisonEffect();

    assertNotSame(first, second);
    assertEquals(first.getType(), second.getType());
  }
}