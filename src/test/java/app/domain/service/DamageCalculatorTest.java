package app.domain.service;

import app.domain.enums.ElementalType;
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

    attack = new Attack("Flamethrower", 40, 100, 10, List.of());

    attacker = new Pokemon(
        "CharSal",
        ElementalType.FIRE,
        100,
        50,
        40,
        30,
        List.of(attack)
    );

    defender = new Pokemon(
        "BulbaSal",
        ElementalType.GRASS,
        100,
        50,
        40,
        30,
        List.of(new Attack("Tackle", 40, 100, 10, List.of()))
    );

    neutralTerrain = new TerrainEffect(1.0, 1.0, 0.0, 0.0);
  }

  // Calculates damage using the attack and effective attributes.
  @Test
  void shouldCalculateBaseDamage() {
    int damage = damageCalculator.calculateDamage(
        attack, attacker, defender, 1.0, neutralTerrain, false
    );

    assertEquals(50, damage);
  }

  // Applies increased damage from type effectiveness.
  @Test
  void shouldApplyTypeEffectivenessMultiplier() {
    int damage = damageCalculator.calculateDamage(
        attack, attacker, defender, 2.0, neutralTerrain, false
    );

    assertEquals(100, damage);
  }

  // Applies reduced damage from type resistance.
  @Test
  void shouldApplyTypeResistanceMultiplier() {
    int damage = damageCalculator.calculateDamage(
        attack, attacker, defender, 0.5, neutralTerrain, false
    );

    assertEquals(25, damage);
  }

  // Applies the terrain damage multiplier.
  @Test
  void shouldApplyFireTerrainMultiplier() {
    TerrainEffect hotAsphalt = new TerrainEffect(1.15, 1.0, 0.0, 0.0);

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
    TerrainEffect hotAsphalt = new TerrainEffect(1.15, 1.0, 0.0, 0.0);

    int damage = damageCalculator.calculateDamage(
        attack, attacker, defender, 2.0, hotAsphalt, true
    );

    assertEquals(172, damage);
  }

  // Applies effective attack and defense attributes.
  @Test
  void shouldUseEffectivePokemonStats() {
    attacker.addStatModifier(
        new app.domain.effect.StatModifier(
            app.domain.enums.StatType.ATTACK,
            2.0
        )
    );

    int damage = damageCalculator.calculateDamage(
        attack, attacker, defender, 1.0, neutralTerrain, false
    );

    assertEquals(100, damage);
  }

  // Ensures damage is at least one point.
  @Test
  void shouldApplyMinimumDamage() {
    Attack weakAttack = new Attack("Weak Attack", 1, 100, 5, List.of());

    int damage = damageCalculator.calculateDamage(
        weakAttack, attacker, defender, 0.01, neutralTerrain, false
    );

    assertEquals(1, damage);
  }

  // Rejects a null attack.
  @Test
  void shouldRejectNullAttack() {
    assertThrows(
        NullPointerException.class,
        () -> damageCalculator.calculateDamage(
            null, attacker, defender, 1.0, neutralTerrain, false
        )
    );
  }

  // Rejects a null attacker.
  @Test
  void shouldRejectNullAttacker() {
    assertThrows(
        NullPointerException.class,
        () -> damageCalculator.calculateDamage(
            attack, null, defender, 1.0, neutralTerrain, false
        )
    );
  }

  // Rejects a null defender.
  @Test
  void shouldRejectNullDefender() {
    assertThrows(
        NullPointerException.class,
        () -> damageCalculator.calculateDamage(
            attack, attacker, null, 1.0, neutralTerrain, false
        )
    );
  }

  // Rejects a null terrain effect.
  @Test
  void shouldRejectNullTerrainEffect() {
    assertThrows(
        NullPointerException.class,
        () -> damageCalculator.calculateDamage(
            attack, attacker, defender, 1.0, null, false
        )
    );
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