
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
        100, 50, 40, 30,
        List.of(createAttack())
    );
  }

  // Creates a Pokemon with valid initial attributes.
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

  // Rejects null or blank Pokemon names.
  @Test
  void shouldRejectInvalidName() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Pokemon(
            null, ElementalType.GRASS,
            100, 50, 40, 30,
            List.of(createAttack())
        )
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> new Pokemon(
            " ", ElementalType.GRASS,
            100, 50, 40, 30,
            List.of(createAttack())
        )
    );
  }

  // Rejects a null elemental type.
  @Test
  void shouldRejectNullType() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Pokemon(
            "Pokemon", null,
            100, 50, 40, 30,
            List.of(createAttack())
        )
    );
  }

  // Rejects non-positive base attributes.
  @Test
  void shouldRejectNonPositiveAttributes() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Pokemon(
            "Pokemon", ElementalType.GRASS,
            0, 50, 40, 30,
            List.of(createAttack())
        )
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> new Pokemon(
            "Pokemon", ElementalType.GRASS,
            100, 0, 40, 30,
            List.of(createAttack())
        )
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> new Pokemon(
            "Pokemon", ElementalType.GRASS,
            100, 50, 0, 30,
            List.of(createAttack())
        )
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> new Pokemon(
            "Pokemon", ElementalType.GRASS,
            100, 50, 40, 0,
            List.of(createAttack())
        )
    );
  }

  // Rejects null or empty attack lists.
  @Test
  void shouldRejectNullOrEmptyAttackList() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Pokemon(
            "Pokemon", ElementalType.GRASS,
            100, 50, 40, 30, null
        )
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> new Pokemon(
            "Pokemon", ElementalType.GRASS,
            100, 50, 40, 30, List.of()
        )
    );
  }

  // Reduces HP and prevents negative health.
  @Test
  void shouldTakeDamageWithoutNegativeHP() {
    Pokemon pokemon = createPokemon();

    pokemon.takeDamage(30);

    assertEquals(70, pokemon.getCurrentHp());
    assertTrue(pokemon.isAlive());

    pokemon.takeDamage(100);

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

  // Restores HP without exceeding the maximum.
  @Test
  void shouldHealWithoutExceedingMaximumHP() {
    Pokemon pokemon = createPokemon();

    pokemon.takeDamage(40);
    pokemon.heal(20);

    assertEquals(80, pokemon.getCurrentHp());

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

  // Applies and combines modifiers of the same stat.
  @Test
  void shouldApplyAndCombineStatModifiers() {
    Pokemon pokemon = createPokemon();

    pokemon.addStatModifier(
        new StatModifier(StatType.ATTACK, 1.5)
    );

    assertEquals(75.0, pokemon.getEffectiveAttack());
    assertEquals(40.0, pokemon.getEffectiveDefense());
    assertEquals(30.0, pokemon.getEffectiveSpeed());

    pokemon.addStatModifier(
        new StatModifier(StatType.ATTACK, 2.0)
    );

    assertEquals(150.0, pokemon.getEffectiveAttack());
  }

  // Removes modifiers only from the selected stat.
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

  // Prevents modifiers from affecting fainted Pokemon.
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

  // Restores PP for all Pokemon attacks.
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
}
