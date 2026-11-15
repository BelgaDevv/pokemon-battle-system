
package app.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttackTest {

  // Creates an attack with valid attributes.
  @Test
  void shouldCreateAttackWithValidAttributes() {
    Attack attack = new Attack(
        "Flame Burst", 50, 90.0, 10, List.of()
    );

    assertEquals("Flame Burst", attack.getName());
    assertEquals(50, attack.getPower());
    assertEquals(90.0, attack.getAccuracy());
    assertEquals(10, attack.getMaxPP());
    assertEquals(10, attack.getCurrentPP());
    assertTrue(attack.canBeUsed());
  }

  // Rejects null or blank names.
  @Test
  void shouldRejectNullOrBlankName() {
    assertThrows(IllegalArgumentException.class,
        () -> new Attack(null, 50, 90, 10, List.of()));

    assertThrows(IllegalArgumentException.class,
        () -> new Attack(" ", 50, 90, 10, List.of()));
  }

  // Rejects non-positive power.
  @Test
  void shouldRejectInvalidPower() {
    assertThrows(IllegalArgumentException.class,
        () -> new Attack("Attack", 0, 90, 10, List.of()));
  }

  // Rejects accuracy outside the valid range.
  @Test
  void shouldRejectInvalidAccuracy() {
    assertThrows(IllegalArgumentException.class,
        () -> new Attack("Attack", 50, -1, 10, List.of()));

    assertThrows(IllegalArgumentException.class,
        () -> new Attack("Attack", 50, 101, 10, List.of()));
  }

  // Accepts minimum and maximum accuracy.
  @Test
  void shouldAcceptAccuracyAtBoundaries() {
    Attack zeroAccuracy = new Attack(
        "Attack", 50, 0, 10, List.of()
    );

    Attack maximumAccuracy = new Attack(
        "Attack", 50, 100, 10, List.of()
    );

    assertEquals(0, zeroAccuracy.getAccuracy());
    assertEquals(100, maximumAccuracy.getAccuracy());
  }

  // Rejects non-positive maximum PP.
  @Test
  void shouldRejectInvalidMaximumPP() {
    assertThrows(IllegalArgumentException.class,
        () -> new Attack("Attack", 50, 90, 0, List.of()));
  }

  // Rejects a null effects list.
  @Test
  void shouldRejectNullEffectsList() {
    assertThrows(IllegalArgumentException.class,
        () -> new Attack("Attack", 50, 90, 10, null));
  }

  // Consumes one PP per use.
  @Test
  void shouldConsumePP() {
    Attack attack = new Attack(
        "Attack", 50, 90, 3, List.of()
    );

    attack.consumePP();

    assertEquals(2, attack.getCurrentPP());
    assertTrue(attack.canBeUsed());
  }

  // Prevents PP from dropping below zero.
  @Test
  void shouldNotAllowPPBelowZero() {
    Attack attack = new Attack(
        "Attack", 50, 90, 1, List.of()
    );

    attack.consumePP();

    assertEquals(0, attack.getCurrentPP());
    assertFalse(attack.canBeUsed());

    assertThrows(
        IllegalStateException.class,
        attack::consumePP
    );
  }

  // Restores PP to its maximum value.
  @Test
  void shouldRestorePPToMaximum() {
    Attack attack = new Attack(
        "Attack", 50, 90, 3, List.of()
    );

    attack.consumePP();
    attack.consumePP();
    attack.restorePP();

    assertEquals(3, attack.getCurrentPP());
    assertTrue(attack.canBeUsed());
  }

  // Prevents modification of the effects list.
  @Test
  void shouldNotAllowModificationOfEffectsList() {
    Attack attack = new Attack(
        "Attack", 50, 90, 3, List.of()
    );

    assertThrows(
        UnsupportedOperationException.class,
        () -> attack.getEffects().add(null)
    );
  }
}
