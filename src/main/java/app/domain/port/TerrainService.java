package app.domain.port;

import app.domain.terrain.Arena;
import app.domain.terrain.TerrainContext;
import app.domain.terrain.TerrainEffect;

/**
 * Defines the contract for terrain-related operations.
 */
public interface TerrainService {

  TerrainContext createBattleContext();

  TerrainEffect calculateEffect(
      Arena arena,
      TerrainContext context
  );
}