
package app.domain.model;

import app.domain.enums.ElementalType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BattleResultTest {

  private Trainer trainer1;
  private Trainer trainer2;
  private Pokemon pokemon1;
  private Pokemon pokemon2;
  private Battle battle;

  @BeforeEach
  void setUp() {
    Attack attack1 = new Attack(
        "Flamethrower", 40, 100, 10, List.of()
    );

    Attack attack2 = new Attack(
        "Water Gun", 40, 100, 10, List.of()
    );

    pokemon1 = new Pokemon(
        "CharSal", ElementalType.FIRE,
        100, 50, 40, 30, List.of(attack1)
    );

    pokemon2 = new Pokemon(
        "SquirtSal", ElementalType.WATER,
        100, 45, 50, 25, List.of(attack2)
    );

    trainer1 = new Trainer("Ash");
    trainer2 = new Trainer("Gary");

    trainer1.setPokemon(pokemon1);
    trainer2.setPokemon(pokemon2);

    battle = new Battle(trainer1, trainer2);
    battle.registerTurn();
    battle.registerTurn();
    battle.finish(trainer1);
  }

  // Stores the winner, loser, Pokemon and turn count.
  @Test
  void shouldCreateResultWithBattleInformation() {
    BattleResult result = new BattleResult(battle);

    assertSame(trainer1, result.getWinner());
    assertSame(trainer2, result.getLoser());
    assertSame(pokemon1, result.getWinnerPokemon());
    assertSame(pokemon2, result.getLoserPokemon());
    assertEquals(2, result.getTurnCount());
  }

  // Identifies the second trainer as winner.
  @Test
  void shouldCreateResultWithTrainer2AsWinner() {
    Battle secondBattle = new Battle(trainer1, trainer2);
    secondBattle.finish(trainer2);

    BattleResult result = new BattleResult(secondBattle);

    assertSame(trainer2, result.getWinner());
    assertSame(trainer1, result.getLoser());
    assertSame(pokemon2, result.getWinnerPokemon());
    assertSame(pokemon1, result.getLoserPokemon());
  }

  // Rejects results from unfinished battles.
  @Test
  void shouldRejectUnfinishedBattle() {
    Battle unfinishedBattle = new Battle(trainer1, trainer2);

    assertThrows(
        IllegalStateException.class,
        () -> new BattleResult(unfinishedBattle)
    );
  }
}
