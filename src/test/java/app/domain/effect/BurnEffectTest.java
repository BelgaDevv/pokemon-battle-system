package app.domain.effect;

import app.domain.enums.StatusType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BurnEffectTest {

  // Verifies that the effect correctly exposes the BURN status type
  @Test
  void shouldHaveBurnStatusType() {
    BurnEffect effect = new BurnEffect();

    assertEquals(StatusType.BURN, effect.getType());
  }

  // Ensures separate effect instances are independently allocated
  @Test
  void shouldCreateIndependentInstances() {
    BurnEffect first = new BurnEffect();
    BurnEffect second = new BurnEffect();

    assertNotSame(first, second);
    assertEquals(first.getType(), second.getType());
  }
}