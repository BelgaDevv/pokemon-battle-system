package app.domain.terrain;

/**
 * Defines the contract for terrains that can affect battle conditions.
 */
public interface Terrain {

  TerrainEffect calculateEffect(TerrainContext context);


  String getName();
}