
package app.domain.tournament;

import app.domain.enums.ElementalType;
import app.domain.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TournamentTest {

  private Trainer createTrainer(String name) {
    Trainer trainer = new Trainer(name);

    Attack attack = new Attack(
        "Tackle", 40, 100, 10, List.of()
    );

    Pokemon pokemon = new Pokemon(
        "BulbaSal",
        ElementalType.GRASS,
        100,
        50,
        40,
        30,
        List.of(attack)
    );

    trainer.setPokemon(pokemon);
    return trainer;
  }

  private BattleResult createBattleResult(
      Trainer winner,
      Trainer loser
  ) {
    Battle battle = new Battle(winner, loser);
    battle.start(winner);
    battle.finish(winner);

    return new BattleResult(battle);
  }

  // Creates a tournament with four participants.
  @Test
  void shouldCreateTournamentWithFourParticipants() {
    List<Trainer> trainers = List.of(
        createTrainer("Trainer 1"),
        createTrainer("Trainer 2"),
        createTrainer("Trainer 3"),
        createTrainer("Trainer 4")
    );

    Tournament tournament = new Tournament(trainers);

    assertEquals(4, tournament.getParticipants().size());
    assertNotNull(tournament.getBracket());
    assertTrue(tournament.getResults().isEmpty());
  }

  // Creates a tournament with eight participants.
  @Test
  void shouldCreateTournamentWithEightParticipants() {
    List<Trainer> trainers = List.of(
        createTrainer("Trainer 1"),
        createTrainer("Trainer 2"),
        createTrainer("Trainer 3"),
        createTrainer("Trainer 4"),
        createTrainer("Trainer 5"),
        createTrainer("Trainer 6"),
        createTrainer("Trainer 7"),
        createTrainer("Trainer 8")
    );

    Tournament tournament = new Tournament(trainers);

    assertEquals(8, tournament.getParticipants().size());
  }

  // Rejects an invalid participant count.
  @Test
  void shouldRejectInvalidParticipantCount() {
    List<Trainer> trainers = List.of(
        createTrainer("Trainer 1"),
        createTrainer("Trainer 2")
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> new Tournament(trainers)
    );
  }

  // Rejects null entries in the participant list.
  @Test
  void shouldRejectNullTrainer() {
    List<Trainer> trainers = java.util.Arrays.asList(
        createTrainer("Trainer 1"),
        createTrainer("Trainer 2"),
        null,
        createTrainer("Trainer 4")
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> new Tournament(trainers)
    );
  }

  // Rejects duplicate participants.
  @Test
  void shouldRejectDuplicateTrainer() {
    Trainer trainer = createTrainer("Trainer 1");

    List<Trainer> trainers = List.of(
        trainer,
        trainer,
        createTrainer("Trainer 3"),
        createTrainer("Trainer 4")
    );

    assertThrows(
        IllegalArgumentException.class,
        () -> new Tournament(trainers)
    );
  }

  // Registers a valid battle result.
  @Test
  void shouldRegisterBattleResult() {
    Trainer winner = createTrainer("Trainer 1");
    Trainer loser = createTrainer("Trainer 2");

    Tournament tournament = new Tournament(List.of(
        winner,
        loser,
        createTrainer("Trainer 3"),
        createTrainer("Trainer 4")
    ));

    BattleResult result = createBattleResult(winner, loser);

    tournament.registerResult(result);

    assertEquals(1, tournament.getResults().size());
    assertSame(result, tournament.getResults().get(0));
  }

  // Rejects results from trainers outside the tournament.
  @Test
  void shouldRejectResultFromExternalTrainers() {
    Trainer winner = createTrainer("External 1");
    Trainer loser = createTrainer("External 2");

    Tournament tournament = new Tournament(List.of(
        createTrainer("Trainer 1"),
        createTrainer("Trainer 2"),
        createTrainer("Trainer 3"),
        createTrainer("Trainer 4")
    ));

    BattleResult result = createBattleResult(winner, loser);

    assertThrows(
        IllegalArgumentException.class,
        () -> tournament.registerResult(result)
    );
  }
}
