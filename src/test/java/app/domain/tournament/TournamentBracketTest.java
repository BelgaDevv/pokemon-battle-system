
package app.domain.tournament;

import app.domain.model.Trainer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class TournamentBracketTest {

  private List<Trainer> createTrainers(int quantity) {
    return IntStream.rangeClosed(1, quantity)
        .mapToObj(index -> new Trainer("Trainer " + index))
        .toList();
  }

  // Creates the initial round with four participants.
  @Test
  void shouldCreateInitialRoundWithFourParticipants() {
    TournamentBracket bracket = new TournamentBracket(
        createTrainers(4)
    );

    assertEquals(2, bracket.getCurrentRound().size());
    assertEquals(1, bracket.getCurrentRoundNumber());
  }

  // Creates the initial round with eight participants.
  @Test
  void shouldCreateInitialRoundWithEightParticipants() {
    TournamentBracket bracket = new TournamentBracket(
        createTrainers(8)
    );

    assertEquals(4, bracket.getCurrentRound().size());
  }

  // Returns the first matchup in the correct order.
  @Test
  void shouldReturnFirstMatch() {
    List<Trainer> trainers = createTrainers(4);
    TournamentBracket bracket = new TournamentBracket(trainers);

    List<Trainer> currentMatch = bracket.getCurrentMatch();

    assertEquals(2, currentMatch.size());
    assertSame(trainers.get(0), currentMatch.get(0));
    assertSame(trainers.get(1), currentMatch.get(1));
  }

  // Advances winners to the next round.
  @Test
  void shouldAdvanceWinnerToNextRound() {
    List<Trainer> trainers = createTrainers(4);
    TournamentBracket bracket = new TournamentBracket(trainers);

    bracket.advanceWinner(trainers.get(0));
    bracket.advanceWinner(trainers.get(2));

    assertEquals(2, bracket.getCurrentRoundNumber());
    assertEquals(1, bracket.getCurrentRound().size());
    assertSame(
        trainers.get(0),
        bracket.getCurrentMatch().get(0)
    );
    assertSame(
        trainers.get(2),
        bracket.getCurrentMatch().get(1)
    );
  }

  // Declares the final winner as champion.
  @Test
  void shouldDeclareChampionAfterFinal() {
    List<Trainer> trainers = createTrainers(4);
    TournamentBracket bracket = new TournamentBracket(trainers);

    bracket.advanceWinner(trainers.get(0));
    bracket.advanceWinner(trainers.get(2));
    bracket.advanceWinner(trainers.get(0));

    assertTrue(bracket.isFinished());
    assertSame(trainers.get(0), bracket.getChampion());
  }

  // Rejects a winner outside the current matchup.
  @Test
  void shouldRejectWinnerOutsideCurrentMatch() {
    List<Trainer> trainers = createTrainers(4);
    TournamentBracket bracket = new TournamentBracket(trainers);

    assertThrows(
        IllegalArgumentException.class,
        () -> bracket.advanceWinner(trainers.get(2))
    );
  }

  // Prevents champion access before the final.
  @Test
  void shouldRejectChampionAccessBeforeTournamentEnds() {
    TournamentBracket bracket = new TournamentBracket(
        createTrainers(4)
    );

    assertThrows(
        IllegalStateException.class,
        bracket::getChampion
    );
  }

  // Prevents matchup access after the tournament ends.
  @Test
  void shouldRejectMatchAccessAfterTournamentEnds() {
    List<Trainer> trainers = createTrainers(4);
    TournamentBracket bracket = new TournamentBracket(trainers);

    bracket.advanceWinner(trainers.get(0));
    bracket.advanceWinner(trainers.get(2));
    bracket.advanceWinner(trainers.get(0));

    assertThrows(
        IllegalStateException.class,
        bracket::getCurrentMatch
    );
  }
}
