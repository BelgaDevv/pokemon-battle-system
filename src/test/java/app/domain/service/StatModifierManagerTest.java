
package app.domain.service;

import app.domain.effect.StatModifier;
import app.domain.enums.ElementalType;
import app.domain.enums.StatType;
import app.domain.model.Attack;
import app.domain.model.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatModifierManagerTest {

  private StatModifierManager statModifierManager;
  private Pokemon pokemon;

  @BeforeEach
  void setUp() {
    statModifierManager = new StatModifierManager();

    Attack attack = new Attack(
        "Tackle", 30, 100, 10, List.of()
    );

    pokemon = new Pokemon(
        "CharSal",
        ElementalType.FIRE,
        100, 50, 40, 30,
        List.of(attack)
    );
  }

  // Applies modifiers to Attack, Defense and Speed.
  @Test
  void shouldApplyStatModifiers() {
    statModifierManager.applyModifier(
        pokemon,
        new StatModifier(StatType.ATTACK, 2.0)
    );

    statModifierManager.applyModifier(
        pokemon,
        new StatModifier(StatType.DEFENSE, 1.5)
    );

    statModifierManager.applyModifier(
        pokemon,
        new StatModifier(StatType.SPEED, 0.5)
    );

    assertEquals(100.0, pokemon.getEffectiveAttack());
    assertEquals(60.0, pokemon.getEffectiveDefense());
    assertEquals(15.0, pokemon.getEffectiveSpeed());
  }

  // Combines multiple modifiers of the same stat.
  @Test
  void shouldCombineModifiersOfSameStat() {
    statModifierManager.applyModifier(
        pokemon,
        new StatModifier(StatType.ATTACK, 2.0)
    );

    statModifierManager.applyModifier(
        pokemon,
        new StatModifier(StatType.ATTACK, 0.5)
    );

    assertEquals(50.0, pokemon.getEffectiveAttack());
  }

  // Keeps modifiers for different stats independent.
  @Test
  void shouldKeepDifferentStatModifiersIndependent() {
    statModifierManager.applyModifier(
        pokemon,
        new StatModifier(StatType.ATTACK, 2.0)
    );

    statModifierManager.applyModifier(
        pokemon,
        new StatModifier(StatType.SPEED, 0.5)
    );

    assertEquals(100.0, pokemon.getEffectiveAttack());
    assertEquals(15.0, pokemon.getEffectiveSpeed());
    assertEquals(40.0, pokemon.getEffectiveDefense());
  }

  // Removes modifiers only from the selected stat.
  @Test
  void shouldRemoveModifiersOfSelectedStat() {
    statModifierManager.applyModifier(
        pokemon,
        new StatModifier(StatType.ATTACK, 2.0)
    );

    statModifierManager.applyModifier(
        pokemon,
        new StatModifier(StatType.SPEED, 0.5)
    );

    statModifierManager.removeModifiers(
        pokemon,
        StatType.ATTACK
    );

    assertEquals(50.0, pokemon.getEffectiveAttack());
    assertEquals(15.0, pokemon.getEffectiveSpeed());
  }

  // Removes all modifiers applied to the same stat.
  @Test
  void shouldRemoveAllModifiersOfSameStat() {
    statModifierManager.applyModifier(
        pokemon,
        new StatModifier(StatType.ATTACK, 2.0)
    );

    statModifierManager.applyModifier(
        pokemon,
        new StatModifier(StatType.ATTACK, 1.5)
    );

    statModifierManager.removeModifiers(
        pokemon,
        StatType.ATTACK
    );

    assertEquals(50.0, pokemon.getEffectiveAttack());
  }
}
