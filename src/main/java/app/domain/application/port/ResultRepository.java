package app.domain.application.port;

import app.domain.model.BattleResult;

import java.util.List;

/**
 * Defines the operations required to store and retrieve
 * completed battle results.
 */
public interface ResultRepository {

  void save(BattleResult result);

  List<BattleResult> findAll();
}
