package app.domain.application.controller;

import app.domain.application.battle.BattleService;
import app.domain.item.BattleItem;
import app.domain.model.Attack;
import app.domain.model.Battle;
import app.domain.terrain.Arena;

import java.util.Objects;

/**
 * Receives battle requests from the presentation layer
 * and delegates their execution to BattleService.
 */
public class BattleController {

  private final BattleService battleService;

  public BattleController(BattleService battleService) {
    this.battleService = Objects.requireNonNull(
        battleService,
        "Battle service cannot be null."
    );
  }

  /**
   * Starts a battle in the selected arena.
   */
  public void startBattle(Battle battle, Arena arena) {
    battleService.startBattle(battle, arena);
  }

  /**
   * Executes an attack during the active battle.
   */
  public void executeAttack(Battle battle, Attack attack) {
    battleService.executeAttack(battle, attack);
  }

  /**
   * Uses an item during the active battle.
   */
  public void useItem(Battle battle, BattleItem item) {
    battleService.useItem(battle, item);
  }
}
