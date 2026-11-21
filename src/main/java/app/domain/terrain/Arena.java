
package app.domain.terrain;

/**
 * Defines the location of a battle and the terrain
 * responsible for its combat effects.
 */
public class Arena {

  private final String name;
  private final Terrain terrain;

  /**
   * Creates an arena with a name and a terrain.
   * Both values must be valid and non-null.
   */
  public Arena(String name, Terrain terrain) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException(
          "Arena name cannot be null or blank."
      );
    }

    if (terrain == null) {
      throw new IllegalArgumentException(
          "Arena terrain cannot be null."
      );
    }

    this.name = name;
    this.terrain = terrain;
  }

  /**
   * Returns the name of the arena.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the terrain associated with the arena.
   */
  public Terrain getTerrain() {
    return terrain;
  }
}
