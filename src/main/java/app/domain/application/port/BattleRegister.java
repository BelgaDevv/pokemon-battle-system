package app.domain.application.port;

import app.domain.event.BattleEvent;

/**
 * Defines the contract for registering battle events.
 * Allows the battle flow to record events without depending
 * on the concrete logging implementation.
 */
public interface BattleRegister {

  void register(BattleEvent event);
}