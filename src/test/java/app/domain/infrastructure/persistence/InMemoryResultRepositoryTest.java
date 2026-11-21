
package app.domain.infrastructure.persistence;

import app.domain.enums.ElementalType;
import app.domain.model.*;
import app.infrastructure.persistence.InMemoryResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryResultRepositoryTest {

  private InMemoryResultRepository repository;
  private BattleResult result1;
  private BattleResult result2;

  @BeforeEach
  void setUp() {
    repository = new InMemoryResultRepository();

    result1 = createBattleResult("Ash", "Gary");
    result2 = createBattleResult("Brock", "Misty");
  }

  // Saves results and preserves their insertion order.
  @Test
  void shouldSaveResultsInInsertionOrder() {
    assertTrue(repository.findAll().isEmpty());

    repository.save(result1);

    assertEquals(List.of(result1), repository.findAll());

    repository.save(result2);

    assertEquals(
        List.of(result1, result2),
        repository.findAll()
    );
  }

  // Rejects null results without changing the repository.
  @Test
  void shouldRejectNullBattleResult() {
    assertThrows(
        NullPointerException.class,
        () -> repository.save(null)
    );

    assertTrue(repository.findAll().isEmpty());
  }

  // Prevents external modification of stored results.
  @Test
  void shouldReturnUnmodifiableResults() {
    repository.save(result1);

    assertThrows(
        UnsupportedOperationException.class,
        () -> repository.findAll().add(result2)
    );

    assertEquals(
        List.of(result1),
        repository.findAll()
    );
  }

  // Returns a snapshot independent of future saves.
  @Test
  void shouldReturnIndependentResultsSnapshot() {
    List<BattleResult> results = repository.findAll();

    repository.save(result1);

    assertTrue(results.isEmpty());
    assertEquals(
        List.of(result1),
        repository.findAll()
    );
  }

  private BattleResult createBattleResult(
      String winnerName,
      String loserName
  ) {
    Trainer winner = createTrainer(
        winnerName, "CharSal"
    );

    Trainer loser = createTrainer(
        loserName, "SquirtSal"
    );

    Battle battle = new Battle(winner, loser);
    battle.registerTurn();
    battle.finish(winner);

    return new BattleResult(battle);
  }

  private Trainer createTrainer(
      String trainerName,
      String pokemonName
  ) {
    Attack attack = new Attack(
        "Tackle", 40, 100, 10, List.of()
    );

    Pokemon pokemon = new Pokemon(
        pokemonName,
        pokemonName.equals("CharSal")
            ? ElementalType.FIRE
            : ElementalType.WATER,
        100, 50, 40, 30,
        List.of(attack)
    );

    Trainer trainer = new Trainer(trainerName);
    trainer.setPokemon(pokemon);

    return trainer;
  }
}
