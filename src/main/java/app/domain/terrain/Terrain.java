package app.domain.terrain;

/**
 * Defines the contract for terrains that can affect battle conditions.
 */
public interface Terrain {

  /**
   * Calculates the effects provided by this terrain
   * under the specified environmental conditions.
   *
   * @param context the current environmental conditions
   * @return the terrain effects
   */
  TerrainEffect calculateEffect(TerrainContext context);

  /**
   * Returns the terrain's display name.
   *
   * @return the terrain name
   */
  String getName();
}