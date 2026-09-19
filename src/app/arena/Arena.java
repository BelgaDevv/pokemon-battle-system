package app.arena;

/**
 * Represents an arena with a specific name and an associated arena effect.
 */
public class Arena {

  private String name;
  private ArenaEffect arenaEffect;

  /**
   * Constructs a new Arena with the specified effect and name.
   */
  public Arena(ArenaEffect arenaEffect, String name) {
    this.arenaEffect = arenaEffect;
    this.name = name;
  }

  /**
   * Returns the effect associated with this arena.
   */
  public ArenaEffect getArenaEffect() {
    return arenaEffect;
  }

  /**
   * Returns the name of this arena.
   */
  public String getName() {
    return name;
  }
}