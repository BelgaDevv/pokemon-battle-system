package app.domain.terrain;

/**
 * Represents the central garden terrain.
 *
 * Grass Pokemon recover 5% of their maximum HP
 * at the end of each turn.
 */
public class CentralGarden implements Terrain {

  @Override
  public TerrainEffect calculateEffect(TerrainContext context) {
    return new TerrainEffect(
        1.0,
        1.0,
        0.0,
        0.05
    );
  }

  @Override
  public String getName() {
    return "Central Garden";
  }
}