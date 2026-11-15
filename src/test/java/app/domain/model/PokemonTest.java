
package app.domain.model;

import app.domain.effect.StatModifier;
import app.domain.enums.ElementalType;
import app.domain.enums.StatType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {

  private Attack createAttack() {
    return new Attack(
        "Tackle", 40, 100, 10, List.of()
    );
  }

  private Pokemon createPokemon() {
    return new Pokemon(
        "BulbaSal",
        ElementalType.GRASS,
        100,
        50,
        40,
        30,
        List.of(createAttack())
    );
  }

  // Creates a Pokemon with valid attributes.
  @Test
  void shouldCreatePokemonWithValidAttributes() {
    Pokemon pokemon = createPokemon();

    assertEquals("BulbaSal", pokemon.getName());
    assertEquals(ElementalType.GRASS, pokemon.getType());
    assertEquals(100, pokemon.getMaxHp());
    assertEquals(100, pokemon.getCurrentHp());
    assertEquals(50, pokemon.getBaseAttack());
    assertEquals(40, pokemon.getBaseDefense());
    assertEquals(30, pokemon.getBaseSpeed());
    assertTrue(pokemon.isAlive());
  }

  // Rejects null or blank names.
  @Test
  void shouldRejectInvalidName() {
    assertThrows(IllegalArgumentException.class,
        () -> new Pokemon(null, ElementalType.GRASS,
            100, 50, 40, 30, List.of(createAttack())));

    assertThrows(IllegalArgumentException.class,
        () -> new Pokemon(" ", ElementalType.GRASS,
            100, 50, 40, 30, List.of(createAttack())));
  }

  // Rejects a null elemental type.
  @Test
  void shouldRejectNullType() {
    assertThrows(IllegalArgumentException.class,
        () -> new Pokemon("Pokemon", null,
            100, 50, 40, 30, List.of(createAttack())));
  }

  // Rejects non-positive attributes.
  @Test
  void shouldRejectNonPositiveAttributes() {
    assertThrows(IllegalArgumentException.class,
        () -> new Pokemon("Pokemon", ElementalType.GRASS,
            0, 50, 40, 30, List.of(createAttack())));

    assertThrows(IllegalArgumentException.class,
        () -> new Pokemon("Pokemon", ElementalType.GRASS,
            100, 0, 40, 30, List.of(createAttack())));

    assertThrows(IllegalArgumentException.class,
        () -> new Pokemon("Pokemon", ElementalType.GRASS,
            100, 50, 0, 30, List.of(createAttack())));

    assertThrows(IllegalArgumentException.class,
        () -> new Pokemon("Pokemon", ElementalType.GRASS,
            100, 50, 40, 0, List.of(createAttack())));
  }

  // Rejects null or empty attack lists.
  @Test
  void shouldRejectNullOrEmptyAttackList() {
    assertThrows(IllegalArgumentException.class,
        () -> new Pokemon("Pokemon", ElementalType.GRASS,
            100, 50, 40, 30, null));

    assertThrows(IllegalArgumentException.class,
        () -> new Pokemon("Pokemon", ElementalType.GRASS,
            100, 50, 40, 30, List.of()));
  }

  // Reduces HP when taking damage.
  @Test
  void shouldTakeDamage() {
    Pokemon pokemon = createPokemon();

    pokemon.takeDamage(30);

    assertEquals(70, pokemon.getCurrentHp());
    assertTrue(pokemon.isAlive());
  }

  // Prevents HP from dropping below zero.
  @Test
  void shouldNotAllowHPBelowZero() {
    Pokemon pokemon = createPokemon();

    pokemon.takeDamage(150);

    assertEquals(0, pokemon.getCurrentHp());
    assertFalse(pokemon.isAlive());
  }

  // Rejects negative damage.
  @Test
  void shouldRejectNegativeDamage() {
    Pokemon pokemon = createPokemon();

    assertThrows(
        IllegalArgumentException.class,
        () -> pokemon.takeDamage(-1)
    );
  }

  // Restores the Pokemon's HP.
  @Test
  void shouldHealPokemon() {
    Pokemon pokemon = createPokemon();

    pokemon.takeDamage(40);
    pokemon.heal(20);

    assertEquals(80, pokemon.getCurrentHp());
  }

  // Prevents HP from exceeding its maximum.
  @Test
  void shouldNotHealAboveMaximumHP() {
    Pokemon pokemon = createPokemon();

    pokemon.heal(50);

    assertEquals(100, pokemon.getCurrentHp());
  }

  // Prevents healing a fainted Pokemon.
  @Test
  void shouldNotHealFaintedPokemon() {
    Pokemon pokemon = createPokemon();

    pokemon.takeDamage(100);
    pokemon.heal(50);

    assertEquals(0, pokemon.getCurrentHp());
    assertFalse(pokemon.isAlive());
  }

  // Rejects negative healing.
  @Test
  void shouldRejectNegativeHealing() {
    Pokemon pokemon = createPokemon();

    assertThrows(
        IllegalArgumentException.class,
        () -> pokemon.heal(-1)
    );
  }

  // Applies a modifier to the selected stat.
  @Test
  void shouldApplyStatModifier() {
    Pokemon pokemon = createPokemon();

    StatModifier modifier = new StatModifier(
        StatType.ATTACK, 1.5
    );

    pokemon.addStatModifier(modifier);

    assertEquals(75.0, pokemon.getEffectiveAttack());
    assertEquals(40.0, pokemon.getEffectiveDefense());
    assertEquals(30.0, pokemon.getEffectiveSpeed());
  }

  // Combines modifiers applied to the same stat.
  @Test
  void shouldCombineModifiersForSameStat() {
    Pokemon pokemon = createPokemon();

    pokemon.addStatModifier(
        new StatModifier(StatType.ATTACK, 1.5)
    );

    pokemon.addStatModifier(
        new StatModifier(StatType.ATTACK, 2.0)
    );

    assertEquals(150.0, pokemon.getEffectiveAttack());
  }

  // Removes modifiers from a specific stat.
  @Test
  void shouldRemoveModifiersOfSpecificStat() {
    Pokemon pokemon = createPokemon();

    pokemon.addStatModifier(
        new StatModifier(StatType.ATTACK, 1.5)
    );

    pokemon.addStatModifier(
        new StatModifier(StatType.DEFENSE, 2.0)
    );

    pokemon.removeStatModifiers(StatType.ATTACK);

    assertEquals(50.0, pokemon.getEffectiveAttack());
    assertEquals(80.0, pokemon.getEffectiveDefense());
    assertEquals(1, pokemon.getStatModifiers().size());
  }

  // Rejects a null stat modifier.
  @Test
  void shouldRejectNullStatModifier() {
    Pokemon pokemon = createPokemon();

    assertThrows(
        IllegalArgumentException.class,
        () -> pokemon.addStatModifier(null)
    );
  }

  // Prevents modifiers from being applied to fainted Pokemon.
  @Test
  void shouldNotApplyModifierToFaintedPokemon() {
    Pokemon pokemon = createPokemon();

    pokemon.takeDamage(100);
    pokemon.addStatModifier(
        new StatModifier(StatType.ATTACK, 2.0)
    );

    assertEquals(50.0, pokemon.getEffectiveAttack());
    assertTrue(pokemon.getStatModifiers().isEmpty());
  }

  // Clears stat modifiers when the Pokemon faints.
  @Test
  void shouldClearStatModifiersWhenPokemonFaints() {
    Pokemon pokemon = createPokemon();

    pokemon.addStatModifier(
        new StatModifier(StatType.ATTACK, 2.0)
    );

    pokemon.takeDamage(100);

    assertTrue(pokemon.getStatModifiers().isEmpty());
    assertEquals(50.0, pokemon.getEffectiveAttack());
  }

  // Restores PP for all attacks.
  @Test
  void shouldRestorePPOfAllAttacks() {
    Attack firstAttack = createAttack();
    Attack secondAttack = new Attack(
        "Vine Whip", 45, 95, 5, List.of()
    );

    Pokemon pokemon = new Pokemon(
        "BulbaSal",
        ElementalType.GRASS,
        100, 50, 40, 30,
        List.of(firstAttack, secondAttack)
    );

    firstAttack.consumePP();
    secondAttack.consumePP();

    pokemon.restoreAttacksPP();

    assertEquals(10, firstAttack.getCurrentPP());
    assertEquals(5, secondAttack.getCurrentPP());
  }

  // Prevents modification of internal lists.
  @Test
  void shouldNotAllowModificationOfInternalLists() {
    Pokemon pokemon = createPokemon();

    assertThrows(
        UnsupportedOperationException.class,
        () -> pokemon.getAttacks().clear()
    );

    assertThrows(
        UnsupportedOperationException.class,
        () -> pokemon.getStatusEffects().clear()
    );

    assertThrows(
        UnsupportedOperationException.class,
        () -> pokemon.getStatModifiers().clear()
    );
  }
}
