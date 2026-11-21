
package app.infrastructure.persistence;

import app.domain.application.port.ResultRepository;
import app.domain.model.BattleResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Stores completed battle results in memory
 * and allows them to be retrieved.
 */
public class InMemoryResultRepository implements ResultRepository {

  private final List<BattleResult> results;

  public InMemoryResultRepository() {
    this.results = new ArrayList<>();
  }

  @Override
  public void save(BattleResult result) {
    results.add(Objects.requireNonNull(
        result, "Battle result cannot be null."
    ));
  }

  /**
   * Returns an unmodifiable copy of the stored results.
   */
  @Override
  public List<BattleResult> findAll() {
    return Collections.unmodifiableList(
        new ArrayList<>(results)
    );
  }
}
