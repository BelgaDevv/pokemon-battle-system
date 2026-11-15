package app.domain.item;

import app.domain.effect.BurnEffect;
import app.domain.effect.PoisonEffect;
import app.domain.enums.ElementalType;
import app.domain.enums.StatusType;
import app.domain.model.Attack;
import app.domain.model.Pokemon;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AntidoteTest {

  private Pokemon createPokemon() {
    Attack attack = new Attack("Tackle", 40, 100, 10, List.of());

    return new Pokemon(
        "SquirtSal",
        ElementalType.WATER,
        100,
        50,
        40,
        30,
        List.of(attack)
    );
  }

  // Returns the correct item name.
  @Test
  void shouldReturnCorrectName() {
    Antidote antidote = new Antidote();

    assertEquals("Antidote", antidote.getName());
  }

  // Removes the poison status.
  @Test
  void shouldRemovePoisonStatus() {
    Pokemon pokemon = createPokemon();
    pokemon.addStatusEffect(new PoisonEffect());

    Antidote antidote = new Antidote();
    antidote.use(pokemon);

    assertFalse(pokemon.hasStatusEffect(StatusType.POISON));
  }

  // Preserves other status effects.
  @Test
  void shouldPreserveOtherStatusEffects() {
    Pokemon pokemon = createPokemon();
    pokemon.addStatusEffect(new PoisonEffect());
    pokemon.addStatusEffect(new BurnEffect());

    Antidote antidote = new Antidote();
    antidote.use(pokemon);

    assertFalse(pokemon.hasStatusEffect(StatusType.POISON));
    assertTrue(pokemon.hasStatusEffect(StatusType.BURN));
  }

  // Leaves Pokemon without poison unchanged.
  @Test
  void shouldNotChangePokemonWithoutPoison() {
    Pokemon pokemon = createPokemon();
    pokemon.addStatusEffect(new BurnEffect());

    Antidote antidote = new Antidote();
    antidote.use(pokemon);

    assertTrue(pokemon.hasStatusEffect(StatusType.BURN));
    assertFalse(pokemon.hasStatusEffect(StatusType.POISON));
  }

  // Rejects a null target.
  @Test
  void shouldRejectNullTarget() {
    Antidote antidote = new Antidote();

    assertThrows(
        IllegalArgumentException.class,
        () -> antidote.use(null)
    );
  }
}