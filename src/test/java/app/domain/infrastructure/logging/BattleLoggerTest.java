package app.domain.infrastructure.logging;

import app.domain.enums.BattleEventType;
import app.domain.event.BattleEvent;
import app.infrastructure.logging.BattleLogger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BattleLoggerTest {

  // Registers an event.
  @Test
  void shouldRegisterBattleEvent() {
    BattleLogger logger = new BattleLogger();
    BattleEvent event = new BattleEvent(
        BattleEventType.ATTACK,
        "Pikachu used Thunderbolt."
    );

    logger.register(event);

    assertEquals(1, logger.getEvents().size());
    assertEquals(event, logger.getEvents().get(0));
  }

  // Preserves event registration order.
  @Test
  void shouldPreserveEventOrder() {
    BattleLogger logger = new BattleLogger();

    BattleEvent attack = new BattleEvent(
        BattleEventType.ATTACK,
        "Attack used."
    );

    BattleEvent damage = new BattleEvent(
        BattleEventType.DAMAGE,
        "Damage dealt."
    );

    logger.register(attack);
    logger.register(damage);

    assertEquals(attack, logger.getEvents().get(0));
    assertEquals(damage, logger.getEvents().get(1));
  }

  // Rejects null events.
  @Test
  void shouldRejectNullEvent() {
    BattleLogger logger = new BattleLogger();

    assertThrows(
        NullPointerException.class,
        () -> logger.register(null)
    );
  }

  // Prevents external list modification.
  @Test
  void shouldReturnUnmodifiableEventList() {
    BattleLogger logger = new BattleLogger();

    logger.register(new BattleEvent(
        BattleEventType.ATTACK,
        "Attack used."
    ));

    assertThrows(
        UnsupportedOperationException.class,
        () -> logger.getEvents().clear()
    );
  }
}
