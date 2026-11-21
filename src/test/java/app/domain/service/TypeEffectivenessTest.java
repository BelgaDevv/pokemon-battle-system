
package app.domain.service;

import app.domain.enums.ElementalType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TypeEffectivenessTest {

  private final TypeEffectiveness typeEffectiveness =
      new TypeEffectiveness();

  // Applies double damage against weaker elemental types.
  @ParameterizedTest
  @CsvSource({
      "FIRE, GRASS",
      "WATER, FIRE",
      "GRASS, WATER"
  })
  void shouldReturnDoubleMultiplier(
      ElementalType attacker,
      ElementalType defender
  ) {
    assertEquals(
        2.0,
        typeEffectiveness.calculateMultiplier(attacker, defender)
    );
  }

  // Applies reduced damage against resistant elemental types.
  @ParameterizedTest
  @CsvSource({
      "FIRE, WATER",
      "WATER, GRASS",
      "GRASS, FIRE"
  })
  void shouldReturnHalfMultiplier(
      ElementalType attacker,
      ElementalType defender
  ) {
    assertEquals(
        0.5,
        typeEffectiveness.calculateMultiplier(attacker, defender)
    );
  }

  // Applies neutral damage against the same elemental type.
  @Test
  void shouldReturnNeutralMultiplierForSameTypes() {
    for (ElementalType type : ElementalType.values()) {
      assertEquals(
          1.0,
          typeEffectiveness.calculateMultiplier(type, type)
      );
    }
  }

  // Rejects null elemental types.
  @Test
  void shouldRejectNullElementalTypes() {
    assertThrows(
        NullPointerException.class,
        () -> typeEffectiveness.calculateMultiplier(
            null,
            ElementalType.GRASS
        )
    );

    assertThrows(
        NullPointerException.class,
        () -> typeEffectiveness.calculateMultiplier(
            ElementalType.FIRE,
            null
        )
    );
  }
}
