
package app.domain.terrain;

/**
 * Provides a healing bonus that restores 5% of maximum HP
 * to Grass Pokemon at the end of each turn.
 */
public class CentralGarden implements Terrain {

  /**
   * Returns the terrain effect with a 5% healing bonus
   * for Grass Pokemon.
   */
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
