
package app.domain.application.tournament;

import app.domain.application.battle.BattleService;
import app.domain.model.Battle;
import app.domain.model.BattleResult;
import app.domain.model.Pokemon;
import app.domain.model.Trainer;
import app.domain.terrain.Arena;
import app.domain.tournament.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentManagerTest {

  @Mock
  private BattleService battleService;

  @Mock
  private Arena arena;

  private Trainer trainer1;
  private Trainer trainer2;
  private Trainer trainer3;
  private Trainer trainer4;
  private Tournament tournament;
  private TournamentManager manager;

  @BeforeEach
  void setUp() {
    trainer1 = createTrainer("Trainer 1");
    trainer2 = createTrainer("Trainer 2");
    trainer3 = createTrainer("Trainer 3");
    trainer4 = createTrainer("Trainer 4");

    tournament = new Tournament(
        List.of(trainer1, trainer2, trainer3, trainer4)
    );

    manager = new TournamentManager(tournament, battleService);
  }

  private Trainer createTrainer(String name) {
    Trainer trainer = new Trainer(name);
    trainer.setPokemon(mock(Pokemon.class));
    return trainer;
  }

  // Advances winners and identifies the tournament champion.
  @Test
  void shouldAdvanceWinnersAndDeclareChampion() {
    Battle first = manager.startNextMatch(arena);

    assertSame(trainer1, first.getTrainer1());
    assertSame(trainer2, first.getTrainer2());

    first.finish(trainer1);

    BattleResult firstResult = manager.completeCurrentMatch();

    assertSame(trainer1, firstResult.getWinner());

    Battle second = manager.startNextMatch(arena);

    assertSame(trainer3, second.getTrainer1());
    assertSame(trainer4, second.getTrainer2());

    second.finish(trainer4);
    manager.completeCurrentMatch();

    Battle finalMatch = manager.startNextMatch(arena);

    assertSame(trainer1, finalMatch.getTrainer1());
    assertSame(trainer4, finalMatch.getTrainer2());

    finalMatch.finish(trainer4);
    manager.completeCurrentMatch();

    assertTrue(manager.isFinished());
    assertSame(trainer4, tournament.getBracket().getChampion());
    assertEquals(3, tournament.getResults().size());

    verify(battleService, times(3))
        .startBattle(any(Battle.class), eq(arena));
  }

  // Prevents starting another match before registering the current result.
  @Test
  void shouldRejectNextMatchUntilCurrentMatchIsCompleted() {
    Battle first = manager.startNextMatch(arena);

    assertThrows(
        IllegalStateException.class,
        () -> manager.startNextMatch(arena)
    );

    first.finish(trainer1);

    assertThrows(
        IllegalStateException.class,
        () -> manager.startNextMatch(arena)
    );

    manager.completeCurrentMatch();

    assertDoesNotThrow(() -> manager.startNextMatch(arena));

    verify(battleService, times(2))
        .startBattle(any(Battle.class), eq(arena));
  }

  // Rejects completion when no match is active.
  @Test
  void shouldRejectCompletionWithoutCurrentMatch() {
    assertThrows(
        IllegalStateException.class,
        manager::completeCurrentMatch
    );

    verifyNoInteractions(battleService);
  }

  // Rejects completion of an unfinished match.
  @Test
  void shouldRejectCompletionOfUnfinishedMatch() {
    manager.startNextMatch(arena);

    assertThrows(
        IllegalStateException.class,
        manager::completeCurrentMatch
    );

    assertTrue(tournament.getResults().isEmpty());
  }

  // Prevents starting matches after the tournament finishes.
  @Test
  void shouldRejectMatchAfterTournamentFinishes() {
    for (Trainer winner : List.of(trainer1, trainer3, trainer1)) {
      Battle match = manager.startNextMatch(arena);
      match.finish(winner);
      manager.completeCurrentMatch();
    }

    assertTrue(manager.isFinished());

    assertThrows(
        IllegalStateException.class,
        () -> manager.startNextMatch(arena)
    );
  }
}
