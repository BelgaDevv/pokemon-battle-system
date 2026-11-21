package app.domain.application.port;

import app.domain.terrain.Arena;
import app.domain.terrain.TerrainContext;
import app.domain.terrain.TerrainEffect;

/**
 * Defines the terrain operations required by the battle flow.
 * Allows BattleService to obtain the battle context and calculate
 * terrain effects without depending on the infrastructure layer.
 */
public interface TerrainService {

  TerrainContext createBattleContext();

  TerrainEffect calculateEffect(
      Arena arena,
      TerrainContext context
  );
}
