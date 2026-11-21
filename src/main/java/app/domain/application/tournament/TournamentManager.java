package app.domain.application.tournament;

import app.domain.application.battle.BattleService;
import app.domain.model.Battle;
import app.domain.model.BattleResult;
import app.domain.model.Trainer;
import app.domain.terrain.Arena;
import app.domain.tournament.Tournament;

import java.util.List;
import java.util.Objects;

/**
 * Coordinates tournament matches by starting battles and advancing
 * their winners through the tournament bracket.
 */
public class TournamentManager {

  private final Tournament tournament;
  private final BattleService battleService;

  private Battle currentBattle;

  public TournamentManager(Tournament tournament, BattleService battleService) {
    this.tournament = Objects.requireNonNull(tournament, "Tournament cannot be null.");

    this.battleService = Objects.requireNonNull(battleService, "BattleService cannot be null.");
  }

  /**
   * Starts the next match after the previous result has been registered.
   */
  public Battle startNextMatch(Arena arena) {
    Objects.requireNonNull(arena, "Arena cannot be null.");

    if (tournament.getBracket().isFinished()) {
      throw new IllegalStateException("The tournament has already finished.");
    }

    if (currentBattle != null) {
      throw new IllegalStateException("The current match must be completed first.");
    }

    List<Trainer> matchup = tournament.getBracket().getCurrentMatch();

    Battle battle = new Battle(matchup.get(0), matchup.get(1));

    battleService.startBattle(battle, arena);

    currentBattle = battle;

    return currentBattle;
  }

  /**
   * Registers the result of the completed match and advances its winner.
   */
  public BattleResult completeCurrentMatch() {
    if (currentBattle == null) {
      throw new IllegalStateException("There is no active tournament match.");
    }

    if (!currentBattle.isFinished()) {
      throw new IllegalStateException("The current match has not finished.");
    }

    BattleResult result = new BattleResult(currentBattle);

    tournament.registerResult(result);

    currentBattle = null;

    return result;
  }

  public Tournament getTournament() {
    return tournament;
  }

  public Battle getCurrentBattle() {
    return currentBattle;
  }

  public boolean isFinished() {
    return tournament.getBracket().isFinished();
  }
}