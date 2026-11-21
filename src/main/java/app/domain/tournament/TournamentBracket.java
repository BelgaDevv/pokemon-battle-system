
package app.domain.tournament;

import app.domain.model.Trainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Organizes knockout matches, advances winners
 * through tournament rounds and determines the champion.
 */
public class TournamentBracket {

  private final List<Trainer> roundWinners;
  private List<List<Trainer>> currentRound;
  private int currentMatchIndex;
  private int roundNumber;
  private Trainer champion;

  /**
   * Creates the initial matches for a tournament
   * with 4 or 8 participants.
   */
  public TournamentBracket(List<Trainer> participants) {
    Objects.requireNonNull(participants, "Participants cannot be null.");

    if (participants.size() != 4 && participants.size() != 8) {
      throw new IllegalArgumentException(
          "A tournament must have 4 or 8 participants."
      );
    }

    this.currentRound = createMatches(participants);
    this.roundWinners = new ArrayList<>();
    this.currentMatchIndex = 0;
    this.roundNumber = 1;
  }

  /**
   * Returns the matches of the current round
   * as unmodifiable lists.
   */
  public List<List<Trainer>> getCurrentRound() {
    List<List<Trainer>> matches = new ArrayList<>();

    for (List<Trainer> match : currentRound) {
      matches.add(Collections.unmodifiableList(match));
    }

    return Collections.unmodifiableList(matches);
  }

  /**
   * Returns the current match without allowing modifications.
   * Throws an exception if the tournament has finished.
   */
  public List<Trainer> getCurrentMatch() {
    if (isFinished()) {
      throw new IllegalStateException(
          "The tournament has already finished."
      );
    }

    return Collections.unmodifiableList(
        currentRound.get(currentMatchIndex)
    );
  }

  /**
   * Returns the current round number, starting at 1.
   */
  public int getCurrentRoundNumber() {
    return roundNumber;
  }

  /**
   * Checks whether a champion has been determined.
   */
  public boolean isFinished() {
    return champion != null;
  }

  /**
   * Returns the tournament champion.
   * Throws an exception if the tournament is still in progress.
   */
  public Trainer getChampion() {
    if (!isFinished()) {
      throw new IllegalStateException(
          "The tournament has not finished."
      );
    }

    return champion;
  }

  /**
   * Registers the winner of the current match and advances
   * the tournament to the next match or round.
   * Determines the champion after the final match.
   */
  public void advanceWinner(Trainer winner) {
    Objects.requireNonNull(winner, "Winner cannot be null.");

    if (isFinished()) {
      throw new IllegalStateException(
          "The tournament has already finished."
      );
    }

    List<Trainer> currentMatch = currentRound.get(currentMatchIndex);

    if (!currentMatch.contains(winner)) {
      throw new IllegalArgumentException(
          "Winner must belong to the current match."
      );
    }

    roundWinners.add(winner);
    currentMatchIndex++;

    if (currentMatchIndex == currentRound.size()) {

      if (roundWinners.size() == 1) {
        champion = roundWinners.get(0);
        return;
      }

      currentRound = createMatches(roundWinners);
      roundWinners.clear();
      currentMatchIndex = 0;
      roundNumber++;
    }
  }

  /**
   * Groups trainers into pairs to create knockout matches.
   */
  private List<List<Trainer>> createMatches(
      List<Trainer> trainers
  ) {
    List<List<Trainer>> matches = new ArrayList<>();

    for (int i = 0; i < trainers.size(); i += 2) {
      Trainer first = Objects.requireNonNull(
          trainers.get(i),
          "Tournament participant cannot be null."
      );

      Trainer second = Objects.requireNonNull(
          trainers.get(i + 1),
          "Tournament participant cannot be null."
      );

      if (first == second) {
        throw new IllegalArgumentException(
            "A trainer cannot compete against themselves."
        );
      }

      matches.add(new ArrayList<>(List.of(first, second)));
    }

    return matches;
  }
}
