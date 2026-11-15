package app.domain.terrain;

/**
 * Represents a slippery terrain with rain puddles.
 *
 * Water attacks receive a 10% damage bonus and a 10%
 * accuracy bonus.
 */
public class RainPuddle implements Terrain {

  @Override
  public TerrainEffect calculateEffect(TerrainContext context) {
    return new TerrainEffect(
        1.0,
        1.10,
        0.10,
        0.0
    );
  }

  @Override
  public String getName() {
    return "Rain Puddle";
  }
}