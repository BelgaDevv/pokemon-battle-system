
package app.domain.service;

import app.domain.effect.StatModifier;
import app.domain.enums.ElementalType;
import app.domain.enums.StatType;
import app.domain.model.Attack;
import app.domain.model.Pokemon;
import app.domain.terrain.TerrainEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DamageCalculatorTest {

  private DamageCalculator damageCalculator;
  private Pokemon attacker;
  private Pokemon defender;
  private Attack attack;
  private TerrainEffect neutralTerrain;

  @BeforeEach
  void setUp() {
    damageCalculator = new DamageCalculator();

    attack = new Attack(
        "Flamethrower", 40, 100, 10, List.of()
    );

    attacker = new Pokemon(
        "CharSal",
        ElementalType.FIRE,
        100, 50, 40, 30,
        List.of(attack)
    );

    defender = new Pokemon(
        "BulbaSal",
        ElementalType.GRASS,
        100, 50, 40, 30,
        List.of(new Attack("Tackle", 40, 100, 10, List.of()))
    );

    neutralTerrain = new TerrainEffect(1.0, 1.0, 0.0, 0.0);
  }

  // Calculates damage using base attack and defense attributes.
  @Test
  void shouldCalculateBaseDamage() {
    int damage = damageCalculator.calculateDamage(
        attack, attacker, defender, 1.0, neutralTerrain, false
    );

    assertEquals(50, damage);
  }

  // Applies type advantage and resistance multipliers.
  @Test
  void shouldApplyTypeEffectivenessMultipliers() {
    int increasedDamage = damageCalculator.calculateDamage(
        attack, attacker, defender, 2.0, neutralTerrain, false
    );

    int reducedDamage = damageCalculator.calculateDamage(
        attack, attacker, defender, 0.5, neutralTerrain, false
    );

    assertEquals(100, increasedDamage);
    assertEquals(25, reducedDamage);
  }

  // Applies the terrain damage multiplier.
  @Test
  void shouldApplyFireTerrainMultiplier() {
    TerrainEffect hotAsphalt = new TerrainEffect(
        1.15, 1.0, 0.0, 0.0
    );

    int damage = damageCalculator.calculateDamage(
        attack, attacker, defender, 1.0, hotAsphalt, false
    );

    assertEquals(57, damage);
  }

  // Applies the critical hit multiplier.
  @Test
  void shouldApplyCriticalHitMultiplier() {
    int damage = damageCalculator.calculateDamage(
        attack, attacker, defender, 1.0, neutralTerrain, true
    );

    assertEquals(75, damage);
  }

  // Combines type, terrain and critical multipliers.
  @Test
  void shouldCombineAllDamageMultipliers() {
    TerrainEffect hotAsphalt = new TerrainEffect(
        1.15, 1.0, 0.0, 0.0
    );

    int damage = damageCalculator.calculateDamage(
        attack, attacker, defender, 2.0, hotAsphalt, true
    );

    assertEquals(172, damage);
  }

  // Uses modified Pokemon attributes when calculating damage.
  @Test
  void shouldUseEffectivePokemonStats() {
    attacker.addStatModifier(
        new StatModifier(StatType.ATTACK, 2.0)
    );

    int damage = damageCalculator.calculateDamage(
        attack, attacker, defender, 1.0, neutralTerrain, false
    );

    assertEquals(100, damage);
  }

  // Ensures that damage is at least one point.
  @Test
  void shouldApplyMinimumDamage() {
    Attack weakAttack = new Attack(
        "Weak Attack", 1, 100, 5, List.of()
    );

    int damage = damageCalculator.calculateDamage(
        weakAttack, attacker, defender, 0.01, neutralTerrain, false
    );

    assertEquals(1, damage);
  }

  // Rejects non-positive type multipliers.
  @Test
  void shouldRejectNonPositiveTypeMultiplier() {
    assertThrows(
        IllegalArgumentException.class,
        () -> damageCalculator.calculateDamage(
            attack, attacker, defender, 0.0, neutralTerrain, false
        )
    );
  }
}
