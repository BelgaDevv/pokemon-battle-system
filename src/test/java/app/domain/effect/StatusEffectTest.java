package app.domain.effect;

import app.domain.enums.StatusType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusEffectTest {

  // Verifies that the status effect correctly stores and returns the assigned type
  @Test
  void shouldStoreStatusType() {
    StatusEffect effect = new StatusEffect(StatusType.BURN) {};

    assertEquals(StatusType.BURN, effect.getType());
  }

  // Ensures multiple distinct status effect types are correctly maintained and separated
  @Test
  void shouldAllowDifferentStatusTypes() {
    StatusEffect burn = new StatusEffect(StatusType.BURN) {};
    StatusEffect poison = new StatusEffect(StatusType.POISON) {};
    StatusEffect paralysis = new StatusEffect(StatusType.PARALYSIS) {};

    assertEquals(StatusType.BURN, burn.getType());
    assertEquals(StatusType.POISON, poison.getType());
    assertEquals(StatusType.PARALYSIS, paralysis.getType());
  }
}