
package app.domain.terrain;

/**
 * Provides neutral terrain effects without modifying
 * attack damage, accuracy or healing.
 */
public class NormalTerrain implements Terrain {

  /**
   * Returns neutral combat modifiers without
   * applying any terrain bonuses.
   */
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
