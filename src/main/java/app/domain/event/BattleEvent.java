
package app.domain.event;

import app.domain.enums.BattleEventType;

import java.util.Objects;

/**
 * Stores the type and description of an event
 * recorded during a battle.
 */
public class BattleEvent {

  private final BattleEventType type;
  private final String description;

  /**
   * Creates a battle event with a valid type
   * and a non-blank description.
   */
  public BattleEvent(BattleEventType type, String description) {
    this.type = Objects.requireNonNull(
        type, "Event type cannot be null."
    );

    if (description == null || description.isBlank()) {
      throw new IllegalArgumentException(
          "Event description cannot be null or blank."
      );
    }

    this.description = description;
  }

  /**
   * Returns the event type.
   */
  public BattleEventType getType() {
    return type;
  }

  /**
   * Returns the description of what happened.
   */
  public String getDescription() {
    return description;
  }
}
