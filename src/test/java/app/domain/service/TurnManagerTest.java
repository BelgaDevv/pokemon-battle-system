
package app.domain.service;

import app.domain.enums.ElementalType;
import app.domain.model.Attack;
import app.domain.model.Pokemon;
import app.domain.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TurnManagerTest {

  private TurnManager turnManager;
  private Trainer trainer1;
  private Trainer trainer2;

  @BeforeEach
  void setUp() {
    turnManager = new TurnManager();

    Attack attack1 = new Attack("Flamethrower", 40, 100, 10, List.of());
    Attack attack2 = new Attack("Water Gun", 40, 100, 10, List.of());

    Pokemon pokemon1 = new Pokemon(
        "CharSal", ElementalType.FIRE,
        100, 50, 40, 30, List.of(attack1)
    );

    Pokemon pokemon2 = new Pokemon(
        "SquirtSal", ElementalType.WATER,
        100, 50, 40, 20, List.of(attack2)
    );

    trainer1 = new Trainer("Trainer 1");
    trainer2 = new Trainer("Trainer 2");

    trainer1.setPokemon(pokemon1);
    trainer2.setPokemon(pokemon2);
  }

  // Selects the trainer with the faster Pokemon.
  @Test
  void shouldSelectTrainerWithHigherSpeed() {
    assertSame(trainer1, turnManager.determineFirstTrainer(trainer1, trainer2));
  }

  // Selects Trainer 1 when both Pokemon have equal speed.
  @Test
  void shouldSelectTrainer1WhenSpeedsAreEqual() {
    Attack attack = new Attack("Tackle", 30, 100, 10, List.of());

    Pokemon pokemon2 = new Pokemon(
        "SquirtSal", ElementalType.WATER,
        100, 50, 40, 30, List.of(attack)
    );

    trainer2.setPokemon(pokemon2);

    assertSame(trainer1, turnManager.determineFirstTrainer(trainer1, trainer2));
  }

  // Selects Trainer 2 when its Pokemon is faster.
  @Test
  void shouldSelectTrainer2WhenItsPokemonIsFaster() {
    Attack attack = new Attack("Water Gun", 40, 100, 10, List.of());

    Pokemon fasterPokemon = new Pokemon(
        "SquirtSal", ElementalType.WATER,
        100, 50, 40, 40, List.of(attack)
    );

    trainer2.setPokemon(fasterPokemon);

    assertSame(trainer2, turnManager.determineFirstTrainer(trainer1, trainer2));
  }

  // Considers stat modifiers when comparing speed.
  @Test
  void shouldUseEffectiveSpeedWhenDeterminingTurnOrder() {
    trainer2.getPokemon().addStatModifier(
        new app.domain.effect.StatModifier(
            app.domain.enums.StatType.SPEED, 2.0
        )
    );

    assertSame(trainer2, turnManager.determineFirstTrainer(trainer1, trainer2));
  }

  // Rejects a null first trainer.
  @Test
  void shouldRejectNullFirstTrainer() {
    assertThrows(
        NullPointerException.class,
        () -> turnManager.determineFirstTrainer(null, trainer2)
    );
  }

  // Rejects a null second trainer.
  @Test
  void shouldRejectNullSecondTrainer() {
    assertThrows(
        NullPointerException.class,
        () -> turnManager.determineFirstTrainer(trainer1, null)
    );
  }

  // Rejects trainers without a Pokemon.
  @Test
  void shouldRejectTrainerWithoutPokemon() {
    Trainer trainerWithoutPokemon = new Trainer("Trainer 3");

    assertThrows(
        IllegalStateException.class,
        () -> turnManager.determineFirstTrainer(trainer1, trainerWithoutPokemon)
    );
  }

  // Rejects when both trainers have no Pokemon.
  @Test
  void shouldRejectBothTrainersWithoutPokemon() {
    Trainer trainerWithoutPokemon1 = new Trainer("Trainer 3");
    Trainer trainerWithoutPokemon2 = new Trainer("Trainer 4");

    assertThrows(
        IllegalStateException.class,
        () -> turnManager.determineFirstTrainer(
            trainerWithoutPokemon1,
            trainerWithoutPokemon2
        )
    );
  }
}
