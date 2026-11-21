
package app.domain.tournament;

import app.domain.model.BattleResult;
import app.domain.model.Trainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Manages tournament participants, match progression
 * and battle results.
 */
public class Tournament {

  private final List<Trainer> participants;
  private final TournamentBracket bracket;
  private final List<BattleResult> results;

  public Tournament(List<Trainer> participants) {
    Objects.requireNonNull(participants, "Participants cannot be null.");

    if (participants.size() != 4 && participants.size() != 8) {
      throw new IllegalArgumentException(
          "A tournament must have 4 or 8 participants."
      );
    }

    List<Trainer> participantCopy = new ArrayList<>(participants);

    if (participantCopy.stream().anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException(
          "Tournament participants cannot be null."
      );
    }

    if (participantCopy.stream().distinct().count()
        != participantCopy.size()) {
      throw new IllegalArgumentException(
          "A trainer cannot participate more than once."
      );
    }

    this.participants = Collections.unmodifiableList(participantCopy);
    this.bracket = new TournamentBracket(this.participants);
    this.results = new ArrayList<>();
  }

  public List<Trainer> getParticipants() {
    return participants;
  }

  public TournamentBracket getBracket() {
    return bracket;
  }

  public List<BattleResult> getResults() {
    return Collections.unmodifiableList(results);
  }

  /**
   * Validates the battle result and advances the winner
   * to the next match in the tournament bracket.
   */
  public void registerResult(BattleResult result) {
    Objects.requireNonNull(result, "Battle result cannot be null.");

    if (bracket.isFinished()) {
      throw new IllegalStateException(
          "The tournament has already finished."
      );
    }

    Trainer winner = result.getWinner();
    Trainer loser = result.getLoser();

    List<Trainer> currentMatch = bracket.getCurrentMatch();

    if (!participants.contains(winner)
        || !participants.contains(loser)) {
      throw new IllegalArgumentException(
          "Battle result trainers must belong to the tournament."
      );
    }

    if (!currentMatch.contains(winner)
        || !currentMatch.contains(loser)
        || winner == loser) {
      throw new IllegalArgumentException(
          "Battle result must match the current tournament matchup."
      );
    }

    bracket.advanceWinner(winner);
    results.add(result);
  }
}
