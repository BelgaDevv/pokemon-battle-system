
package app.domain.model;

import app.domain.enums.BattleEventType;
import app.domain.enums.ElementalType;
import app.domain.event.BattleEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BattleTest {

  private Trainer trainer1;
  private Trainer trainer2;
  private Battle battle;

  @BeforeEach
  void setUp() {
    trainer1 = createTrainer(
        "Ash", "CharSal", ElementalType.FIRE
    );

    trainer2 = createTrainer(
        "Gary", "SquirtSal", ElementalType.WATER
    );

    battle = new Battle(trainer1, trainer2);
  }

  // Creates a battle with the correct initial state.
  @Test
  void shouldCreateBattleWithTwoTrainers() {
    assertSame(trainer1, battle.getTrainer1());
    assertSame(trainer2, battle.getTrainer2());
    assertNull(battle.getWinner());
    assertNull(battle.getCurrentTrainer());
    assertEquals(0, battle.getTurnCount());
    assertFalse(battle.isStarted());
    assertFalse(battle.isFinished());
  }

  // Rejects using the same trainer twice.
  @Test
  void shouldRejectSameTrainer() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Battle(trainer1, trainer1)
    );
  }

  // Starts a battle with either participant.
  @Test
  void shouldStartBattleWithEitherTrainer() {
    battle.start(trainer1);

    assertTrue(battle.isStarted());
    assertSame(trainer1, battle.getCurrentTrainer());

    Battle secondBattle = new Battle(trainer1, trainer2);
    secondBattle.start(trainer2);

    assertTrue(secondBattle.isStarted());
    assertSame(trainer2, secondBattle.getCurrentTrainer());
  }

  // Rejects a trainer outside the battle.
  @Test
  void shouldRejectNonParticipantWhenStarting() {
    Trainer outsider = new Trainer("Brock");

    assertThrows(
        IllegalArgumentException.class,
        () -> battle.start(outsider)
    );
  }

  // Prevents starting without assigned Pokemon.
  @Test
  void shouldRejectStartingWithoutPokemon() {
    Trainer first = new Trainer("Ash");
    Trainer second = new Trainer("Gary");

    Battle invalidBattle = new Battle(first, second);

    assertThrows(
        IllegalStateException.class,
        () -> invalidBattle.start(first)
    );
  }

  // Prevents starting the battle twice.
  @Test
  void shouldPreventStartingBattleTwice() {
    battle.start(trainer1);

    assertThrows(
        IllegalStateException.class,
        () -> battle.start(trainer2)
    );
  }

  // Alternates turns between both trainers.
  @Test
  void shouldAlternateTrainers() {
    battle.start(trainer1);

    battle.advanceTurn();
    assertSame(trainer2, battle.getCurrentTrainer());

    battle.advanceTurn();
    assertSame(trainer1, battle.getCurrentTrainer());
  }

  // Prevents advancing turns before the battle starts.
  @Test
  void shouldRejectAdvancingBeforeStarting() {
    assertThrows(
        IllegalStateException.class,
        battle::advanceTurn
    );
  }

  // Identifies the trainer whose turn is active.
  @Test
  void shouldIdentifyCurrentTrainerTurn() {
    battle.start(trainer1);

    assertTrue(battle.isTrainerTurn(trainer1));
    assertFalse(battle.isTrainerTurn(trainer2));
  }

  // Prevents checking turns before the battle starts.
  @Test
  void shouldRejectCheckingTurnBeforeStarting() {
    assertThrows(
        IllegalStateException.class,
        () -> battle.isTrainerTurn(trainer1)
    );
  }

  // Counts registered turns.
  @Test
  void shouldRegisterMultipleTurns() {
    battle.registerTurn();

    assertEquals(1, battle.getTurnCount());

    battle.registerTurn();
    battle.registerTurn();

    assertEquals(3, battle.getTurnCount());
  }

  // Registers battle events in their original order.
  @Test
  void shouldPreserveBattleEventOrder() {
    BattleEvent attackEvent = new BattleEvent(
        BattleEventType.ATTACK,
        "Ash attacked."
    );

    BattleEvent damageEvent = new BattleEvent(
        BattleEventType.DAMAGE,
        "Gary received damage."
    );

    battle.addBattleEvent(attackEvent);

    assertEquals(1, battle.getBattleLog().size());
    assertSame(attackEvent, battle.getBattleLog().get(0));

    battle.addBattleEvent(damageEvent);

    assertEquals(
        List.of(attackEvent, damageEvent),
        battle.getBattleLog()
    );
  }

  // Finishes a battle with either participant as winner.
  @Test
  void shouldFinishBattleWithEitherTrainerAsWinner() {
    battle.finish(trainer1);

    assertSame(trainer1, battle.getWinner());
    assertTrue(battle.isFinished());

    Battle secondBattle = new Battle(trainer1, trainer2);
    secondBattle.finish(trainer2);

    assertSame(trainer2, secondBattle.getWinner());
    assertTrue(secondBattle.isFinished());
  }

  // Rejects a winner outside the battle.
  @Test
  void shouldRejectTrainerOutsideBattle() {
    Trainer outsider = new Trainer("Brock");

    assertThrows(
        IllegalArgumentException.class,
        () -> battle.finish(outsider)
    );
  }

  // Prevents finishing a battle more than once.
  @Test
  void shouldPreventFinishingBattleTwice() {
    battle.finish(trainer1);

    assertThrows(
        IllegalStateException.class,
        () -> battle.finish(trainer2)
    );
  }

  // Prevents state changes after the battle finishes.
  @Test
  void shouldPreventOperationsAfterFinishing() {
    battle.start(trainer1);
    battle.finish(trainer1);

    BattleEvent event = new BattleEvent(
        BattleEventType.ATTACK,
        "Late event."
    );

    assertAll(
        () -> assertThrows(
            IllegalStateException.class,
            battle::registerTurn
        ),
        () -> assertThrows(
            IllegalStateException.class,
            () -> battle.addBattleEvent(event)
        ),
        () -> assertThrows(
            IllegalStateException.class,
            battle::advanceTurn
        )
    );
  }

  private Trainer createTrainer(
      String trainerName,
      String pokemonName,
      ElementalType type
  ) {
    Attack attack = new Attack(
        "Tackle", 40, 100, 10, List.of()
    );

    Pokemon pokemon = new Pokemon(
        pokemonName, type,
        100, 50, 40, 30,
        List.of(attack)
    );

    Trainer trainer = new Trainer(trainerName);
    trainer.setPokemon(pokemon);

    return trainer;
  }
}
