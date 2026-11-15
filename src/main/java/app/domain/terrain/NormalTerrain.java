package app.domain.terrain;

/**
 * Represents a terrain without special combat effects.
 */
public class NormalTerrain implements Terrain {

  @Override
  public TerrainEffect calculateEffect(TerrainContext context) {
    return new TerrainEffect(
        1.0,
        1.0,
        0.0,
        0.0
    );
  }

  @Override
  public String getName() {
    return "Normal Terrain";
  }
}