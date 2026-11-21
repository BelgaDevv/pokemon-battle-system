
package app.domain.model;

import java.util.Objects;

/**
 * Stores the outcome of a completed battle,
 * including both trainers, their Pokemon and the turn count.
 */
public class BattleResult {

  private final Trainer winner;
  private final Trainer loser;
  private final Pokemon winnerPokemon;
  private final Pokemon loserPokemon;
  private final int turnCount;

  /**
   * Creates a battle result from a finished battle.
   * Identifies the winner and loser and records their
   * Pokemon and the number of turns completed.
   */
  public BattleResult(Battle battle) {
    Objects.requireNonNull(battle, "Battle cannot be null.");

    if (!battle.isFinished()) {
      throw new IllegalStateException(
          "Cannot create a result for an unfinished battle."
      );
    }

    this.winner = Objects.requireNonNull(
        battle.getWinner(),
        "Battle winner cannot be null."
    );

    this.loser = battle.getTrainer1() == winner
        ? battle.getTrainer2()
        : battle.getTrainer1();

    this.winnerPokemon = Objects.requireNonNull(
        winner.getPokemon(),
        "Winner Pokemon cannot be null."
    );

    this.loserPokemon = Objects.requireNonNull(
        loser.getPokemon(),
        "Loser Pokemon cannot be null."
    );

    this.turnCount = battle.getTurnCount();
  }

  /**
   * Returns the winning trainer.
   */
  public Trainer getWinner() {
    return winner;
  }

  /**
   * Returns the losing trainer.
   */
  public Trainer getLoser() {
    return loser;
  }

  /**
   * Returns the Pokemon used by the winning trainer.
   */
  public Pokemon getWinnerPokemon() {
    return winnerPokemon;
  }

  /**
   * Returns the Pokemon used by the losing trainer.
   */
  public Pokemon getLoserPokemon() {
    return loserPokemon;
  }

  /**
   * Returns the number of turns recorded during the battle.
   */
  public int getTurnCount() {
    return turnCount;
  }
}
