
package app.infrastructure.logging;

import app.domain.application.port.BattleRegister;
import app.domain.event.BattleEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Records battle events in memory, preserving
 * the order in which they occur.
 */
public class BattleLogger implements BattleRegister {

  private final List<BattleEvent> events;

  public BattleLogger() {
    this.events = new ArrayList<>();
  }

  @Override
  public void register(BattleEvent event) {
    events.add(Objects.requireNonNull(
        event,
        "Battle event cannot be null."
    ));
  }

  /**
   * Returns the recorded events in registration order
   * as an unmodifiable list.
   */
  public List<BattleEvent> getEvents() {
    return Collections.unmodifiableList(events);
  }
}
