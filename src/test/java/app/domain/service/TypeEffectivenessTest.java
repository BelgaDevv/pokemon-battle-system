package app.domain.service;

import app.domain.enums.ElementalType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TypeEffectivenessTest {

  private final TypeEffectiveness typeEffectiveness = new TypeEffectiveness();

  // Applies increased damage against Grass.
  @Test
  void shouldReturnDoubleMultiplierForFireAgainstGrass() {
    assertEquals(
        2.0,
        typeEffectiveness.calculateMultiplier(
            ElementalType.FIRE,
            ElementalType.GRASS
        )
    );
  }

  // Applies reduced damage against Water.
  @Test
  void shouldReturnHalfMultiplierForFireAgainstWater() {
    assertEquals(
        0.5,
        typeEffectiveness.calculateMultiplier(
            ElementalType.FIRE,
            ElementalType.WATER
        )
    );
  }

  // Applies increased damage against Fire.
  @Test
  void shouldReturnDoubleMultiplierForWaterAgainstFire() {
    assertEquals(
        2.0,
        typeEffectiveness.calculateMultiplier(
            ElementalType.WATER,
            ElementalType.FIRE
        )
    );
  }

  // Applies reduced damage against Grass.
  @Test
  void shouldReturnHalfMultiplierForWaterAgainstGrass() {
    assertEquals(
        0.5,
        typeEffectiveness.calculateMultiplier(
            ElementalType.WATER,
            ElementalType.GRASS
        )
    );
  }

  // Applies increased damage against Water.
  @Test
  void shouldReturnDoubleMultiplierForGrassAgainstWater() {
    assertEquals(
        2.0,
        typeEffectiveness.calculateMultiplier(
            ElementalType.GRASS,
            ElementalType.WATER
        )
    );
  }

  // Applies reduced damage against Fire.
  @Test
  void shouldReturnHalfMultiplierForGrassAgainstFire() {
    assertEquals(
        0.5,
        typeEffectiveness.calculateMultiplier(
            ElementalType.GRASS,
            ElementalType.FIRE
        )
    );
  }

  // Applies neutral damage against the same type.
  @Test
  void shouldReturnNeutralMultiplierForSameTypes() {
    for (ElementalType type : ElementalType.values()) {
      assertEquals(
          1.0,
          typeEffectiveness.calculateMultiplier(type, type)
      );
    }
  }

  // Rejects a null attack type.
  @Test
  void shouldRejectNullAttackType() {
    assertThrows(
        NullPointerException.class,
        () -> typeEffectiveness.calculateMultiplier(
            null,
            ElementalType.GRASS
        )
    );
  }

  // Rejects a null defender type.
  @Test
  void shouldRejectNullDefenderType() {
    assertThrows(
        NullPointerException.class,
        () -> typeEffectiveness.calculateMultiplier(
            ElementalType.FIRE,
            null
        )
    );
  }
}