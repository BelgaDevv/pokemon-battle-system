package app.domain.item;

import app.domain.enums.ElementalType;
import app.domain.model.Attack;
import app.domain.model.Pokemon;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PotionTest {

  private Pokemon createPokemon() {
    Attack attack = new Attack("Tackle", 40, 100, 10, List.of());

    return new Pokemon(
        "BulbaSal",
        ElementalType.GRASS,
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
    Potion potion = new Potion(20);

    assertEquals("Potion", potion.getName());
  }

  // Rejects zero healing amount.
  @Test
  void shouldRejectZeroHealingAmount() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Potion(0)
    );
  }

  // Rejects negative healing amount.
  @Test
  void shouldRejectNegativeHealingAmount() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Potion(-10)
    );
  }

  // Restores Pokemon HP.
  @Test
  void shouldRestorePokemonHp() {
    Pokemon pokemon = createPokemon();
    pokemon.takeDamage(50);

    Potion potion = new Potion(20);
    potion.use(pokemon);

    assertEquals(70, pokemon.getCurrentHp());
  }

  // Prevents HP from exceeding the maximum.
  @Test
  void shouldNotExceedMaximumHp() {
    Pokemon pokemon = createPokemon();
    pokemon.takeDamage(10);

    Potion potion = new Potion(50);
    potion.use(pokemon);

    assertEquals(100, pokemon.getCurrentHp());
  }

  // Does not revive a fainted Pokemon.
  @Test
  void shouldNotReviveFaintedPokemon() {
    Pokemon pokemon = createPokemon();
    pokemon.takeDamage(100);

    Potion potion = new Potion(50);
    potion.use(pokemon);

    assertEquals(0, pokemon.getCurrentHp());
  }

  // Rejects a null target.
  @Test
  void shouldRejectNullTarget() {
    Potion potion = new Potion(20);

    assertThrows(
        IllegalArgumentException.class,
        () -> potion.use(null)
    );
  }
}