
package app.domain.application.catalog;

import app.domain.effect.ApplyStatModifierEffect;
import app.domain.effect.InflictStatusEffect;
import app.domain.enums.ElementalType;
import app.domain.enums.StatType;
import app.domain.model.Attack;
import app.domain.model.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PokemonCatalogTest {

  private PokemonCatalog catalog;

  @BeforeEach
  void setUp() {
    catalog = new PokemonCatalog();
  }

  // Verifies the number and elemental distribution of species.
  @Test
  void shouldReturnSixSpeciesWithCorrectTypes() {
    List<Pokemon> pokemon = catalog.getAvailablePokemon();

    assertEquals(6, pokemon.size());

    assertEquals(2, pokemon.stream()
        .filter(p -> p.getType() == ElementalType.FIRE)
        .count());

    assertEquals(2, pokemon.stream()
        .filter(p -> p.getType() == ElementalType.WATER)
        .count());

    assertEquals(2, pokemon.stream()
        .filter(p -> p.getType() == ElementalType.GRASS)
        .count());
  }

  // Creates a Pokemon with its configured attributes.
  @Test
  void shouldCreateCharmanderWithCorrectAttributes() {
    Pokemon pokemon = catalog.createPokemon("Charmander");

    assertEquals("Charmander", pokemon.getName());
    assertEquals(ElementalType.FIRE, pokemon.getType());
    assertEquals(90, pokemon.getMaxHp());
    assertEquals(52, pokemon.getBaseAttack());
    assertEquals(40, pokemon.getBaseDefense());
    assertEquals(58, pokemon.getBaseSpeed());
    assertEquals(2, pokemon.getAttacks().size());
  }

  // Accepts species names regardless of letter case.
  @Test
  void shouldAcceptDifferentLetterCase() {
    Pokemon pokemon = catalog.createPokemon("cHaRmAnDeR");

    assertEquals("Charmander", pokemon.getName());
  }

  // Rejects unknown, null and blank species names.
  @Test
  void shouldRejectInvalidSpeciesNames() {
    assertThrows(
        IllegalArgumentException.class,
        () -> catalog.createPokemon("Pikachu")
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> catalog.createPokemon(null)
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> catalog.createPokemon(" ")
    );
  }

  // Creates Pokemon with independent health and attack PP.
  @Test
  void shouldCreateIndependentPokemonInstances() {
    Pokemon first = catalog.createPokemon("Squirtle");
    Pokemon second = catalog.createPokemon("Squirtle");

    Attack firstAttack = first.getAttacks().get(0);
    Attack secondAttack = second.getAttacks().get(0);

    first.takeDamage(20);
    firstAttack.consumePP();

    assertEquals(85, first.getCurrentHp());
    assertEquals(105, second.getCurrentHp());

    assertEquals(
        firstAttack.getMaxPP() - 1,
        firstAttack.getCurrentPP()
    );

    assertEquals(
        secondAttack.getMaxPP(),
        secondAttack.getCurrentPP()
    );

    assertNotSame(firstAttack, secondAttack);
  }

  // Configures status effects for the expected species.
  @Test
  void shouldConfigureStatusAttacks() {
    Pokemon charmander = catalog.createPokemon("Charmander");
    Pokemon bulbasaur = catalog.createPokemon("Bulbasaur");
    Pokemon chikorita = catalog.createPokemon("Chikorita");

    assertInstanceOf(
        InflictStatusEffect.class,
        charmander.getAttacks().get(1).getEffects().get(0)
    );

    assertInstanceOf(
        InflictStatusEffect.class,
        bulbasaur.getAttacks().get(1).getEffects().get(0)
    );

    assertInstanceOf(
        InflictStatusEffect.class,
        chikorita.getAttacks().get(1).getEffects().get(0)
    );
  }

  // Configures the correct attributes and multipliers.
  @Test
  void shouldConfigureStatModifierAttacks() {
    Pokemon cyndaquil = catalog.createPokemon("Cyndaquil");
    Pokemon squirtle = catalog.createPokemon("Squirtle");
    Pokemon totodile = catalog.createPokemon("Totodile");

    ApplyStatModifierEffect attackEffect = assertInstanceOf(
        ApplyStatModifierEffect.class,
        cyndaquil.getAttacks().get(1).getEffects().get(0)
    );

    ApplyStatModifierEffect defenseEffect = assertInstanceOf(
        ApplyStatModifierEffect.class,
        squirtle.getAttacks().get(1).getEffects().get(0)
    );

    ApplyStatModifierEffect reductionEffect = assertInstanceOf(
        ApplyStatModifierEffect.class,
        totodile.getAttacks().get(1).getEffects().get(0)
    );

    assertEquals(
        StatType.ATTACK,
        attackEffect.getStatModifier().getStatType()
    );

    assertEquals(
        StatType.DEFENSE,
        defenseEffect.getStatModifier().getStatType()
    );

    assertEquals(
        StatType.DEFENSE,
        reductionEffect.getStatModifier().getStatType()
    );

    assertEquals(
        1.20,
        attackEffect.getStatModifier().getMultiplier()
    );

    assertEquals(
        1.20,
        defenseEffect.getStatModifier().getMultiplier()
    );

    assertEquals(
        0.80,
        reductionEffect.getStatModifier().getMultiplier()
    );
  }

  // Verifies that every catalog species can be created.
  @Test
  void shouldCreateEveryAvailableSpecies() {
    for (Pokemon available : catalog.getAvailablePokemon()) {
      Pokemon created = catalog.createPokemon(available.getName());

      assertEquals(available.getName(), created.getName());
      assertEquals(available.getType(), created.getType());
      assertEquals(2, created.getAttacks().size());
    }
  }
}
