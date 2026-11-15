package app.domain.terrain;

/**
 * Represents the location where a battle takes place.
 *
 * An arena contains a terrain that determines its combat effects.
 */
public class Arena {

  private final String name;
  private final Terrain terrain;

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

  public String getName() {
    return name;
  }

  public Terrain getTerrain() {
    return terrain;
  }
}