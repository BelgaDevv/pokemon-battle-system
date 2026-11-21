
package app.domain.model;

import app.domain.enums.ElementalType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainerTest {

  private Pokemon createPokemon() {
    Attack attack = new Attack(
        "Tackle", 40, 100, 10, List.of()
    );

    return new Pokemon(
        "BulbaSal",
        ElementalType.GRASS,
        100, 50, 40, 30,
        List.of(attack)
    );
  }

  // Creates a trainer with the correct initial state.
  @Test
  void shouldCreateTrainerWithValidName() {
    Trainer trainer = new Trainer("Ash");

    assertEquals("Ash", trainer.getName());
    assertNull(trainer.getPokemon());
    assertEquals(0, trainer.getItemsUsed());
    assertTrue(trainer.canUseItem());
  }

  // Rejects null or blank trainer names.
  @Test
  void shouldRejectNullOrBlankName() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Trainer(null)
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> new Trainer(" ")
    );
  }

  // Assigns a Pokemon to the trainer.
  @Test
  void shouldAssignPokemonToTrainer() {
    Trainer trainer = new Trainer("Ash");
    Pokemon pokemon = createPokemon();

    trainer.setPokemon(pokemon);

    assertSame(pokemon, trainer.getPokemon());
  }

  // Allows two item uses and rejects a third.
  @Test
  void shouldEnforceItemUsageLimit() {
    Trainer trainer = new Trainer("Ash");

    trainer.registerItemUse();

    assertEquals(1, trainer.getItemsUsed());
    assertTrue(trainer.canUseItem());

    trainer.registerItemUse();

    assertEquals(2, trainer.getItemsUsed());
    assertFalse(trainer.canUseItem());

    assertThrows(
        IllegalStateException.class,
        trainer::registerItemUse
    );

    assertEquals(2, trainer.getItemsUsed());
  }

  // Resets item usage for a new battle.
  @Test
  void shouldResetItemUsage() {
    Trainer trainer = new Trainer("Ash");

    trainer.registerItemUse();
    trainer.registerItemUse();

    trainer.resetItemsUsed();

    assertEquals(0, trainer.getItemsUsed());
    assertTrue(trainer.canUseItem());
  }
}
