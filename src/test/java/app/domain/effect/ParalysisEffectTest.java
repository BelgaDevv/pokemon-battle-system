package app.domain.effect;

import app.domain.enums.StatusType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class ParalysisEffectTest {

  // Verifies that the effect correctly exposes the PARALYSIS status type
  @Test
  void shouldHaveParalysisStatusType() {
    ParalysisEffect effect = new ParalysisEffect();

    assertEquals(StatusType.PARALYSIS, effect.getType());
  }

  // Ensures separate effect instances are independently allocated
  @Test
  void shouldCreateIndependentInstances() {
    ParalysisEffect first = new ParalysisEffect();
    ParalysisEffect second = new ParalysisEffect();

    assertNotSame(first, second);
    assertEquals(first.getType(), second.getType());
  }
}