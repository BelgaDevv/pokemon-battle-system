
package app.domain.model;

import app.domain.event.BattleEvent;
import app.domain.terrain.TerrainContext;
import app.domain.terrain.TerrainEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Manages the state of a battle between two trainers,
 * including turn order, terrain effects, events and the winner.
 */
public class Battle {

  private final Trainer trainer1;
  private final Trainer trainer2;
  private final List<BattleEvent> battleLog;

  private Trainer currentTrainer;
  private Trainer winner;
  private TerrainEffect terrainEffect;
  private int turnCount;
  private boolean started;
  private boolean finished;
  private TerrainContext terrainContext;

  /**
   * Creates a battle between two different trainers.
   * The battle remains inactive until it is started.
   */
  public Battle(Trainer trainer1, Trainer trainer2) {
    this.trainer1 = Objects.requireNonNull(
        trainer1, "Trainer 1 cannot be null."
    );

    this.trainer2 = Objects.requireNonNull(
        trainer2, "Trainer 2 cannot be null."
    );

    if (trainer1 == trainer2) {
      throw new IllegalArgumentException(
          "A trainer cannot battle against themselves."
      );
    }

    this.battleLog = new ArrayList<>();
    this.turnCount = 0;
    this.started = false;
    this.finished = false;
  }

  /**
   * Returns the environmental context assigned to the battle.
   * Throws an exception if it has not been initialized.
   */
  public TerrainContext getTerrainContext() {
    if (terrainContext == null) {
      throw new IllegalStateException(
          "Battle terrain context has not been initialized."
      );
    }

    return terrainContext;
  }

  /**
   * Returns the first battle participant.
   */
  public Trainer getTrainer1() {
    return trainer1;
  }

  /**
   * Returns the second battle participant.
   */
  public Trainer getTrainer2() {
    return trainer2;
  }

  /**
   * Returns the trainer whose turn is currently active.
   */
  public Trainer getCurrentTrainer() {
    return currentTrainer;
  }

  /**
   * Returns the winner, or null if none has been assigned.
   */
  public Trainer getWinner() {
    return winner;
  }

  /**
   * Returns the number of registered turns.
   */
  public int getTurnCount() {
    return turnCount;
  }

  /**
   * Checks whether the battle has started.
   */
  public boolean isStarted() {
    return started;
  }

  /**
   * Checks whether the battle has finished.
   */
  public boolean isFinished() {
    return finished;
  }

  /**
   * Returns the recorded battle events
   * as an unmodifiable list.
   */
  public List<BattleEvent> getBattleLog() {
    return Collections.unmodifiableList(battleLog);
  }

  /**
   * Returns the terrain effect assigned to the battle.
   * Throws an exception if it has not been initialized.
   */
  public TerrainEffect getTerrainEffect() {
    if (terrainEffect == null) {
      throw new IllegalStateException(
          "Battle terrain effect has not been initialized."
      );
    }

    return terrainEffect;
  }

  /**
   * Starts the battle with the first trainer,
   * terrain effect and environmental context.
   */
  public void start(
      Trainer firstTrainer,
      TerrainEffect terrainEffect,
      TerrainContext terrainContext
  ) {
    Objects.requireNonNull(
        terrainContext,
        "Terrain context cannot be null."
    );

    start(firstTrainer, terrainEffect);
    this.terrainContext = terrainContext;
  }

  /**
   * Starts the battle and assigns the first trainer.
   * Both participants must have a Pokemon.
   */
  public void start(Trainer firstTrainer) {
    if (finished) {
      throw new IllegalStateException(
          "Cannot start a finished battle."
      );
    }

    if (started) {
      throw new IllegalStateException(
          "Battle has already started."
      );
    }

    Objects.requireNonNull(
        firstTrainer, "First trainer cannot be null."
    );

    if (firstTrainer != trainer1 && firstTrainer != trainer2) {
      throw new IllegalArgumentException(
          "First trainer must be a battle participant."
      );
    }

    if (trainer1.getPokemon() == null || trainer2.getPokemon() == null) {
      throw new IllegalStateException(
          "Both trainers must have a Pokemon before battle."
      );
    }

    this.currentTrainer = firstTrainer;
    this.started = true;
  }

  /**
   * Starts the battle with the first trainer
   * and the specified terrain effect.
   */
  public void start(Trainer firstTrainer, TerrainEffect terrainEffect) {
    Objects.requireNonNull(
        terrainEffect,
        "Terrain effect cannot be null."
    );

    start(firstTrainer);
    this.terrainEffect = terrainEffect;
  }

  /**
   * Passes the active turn to the other trainer.
   * The battle must be started and unfinished.
   */
  public void advanceTurn() {
    if (finished) {
      throw new IllegalStateException(
          "Cannot advance turns after the battle has finished."
      );
    }

    if (!started) {
      throw new IllegalStateException(
          "Battle has not started."
      );
    }

    currentTrainer = currentTrainer == trainer1
        ? trainer2
        : trainer1;
  }

  /**
   * Checks whether the specified trainer
   * is responsible for the current turn.
   */
  public boolean isTrainerTurn(Trainer trainer) {
    Objects.requireNonNull(trainer, "Trainer cannot be null.");

    if (!started) {
      throw new IllegalStateException(
          "Battle has not started."
      );
    }

    return currentTrainer == trainer;
  }

  /**
   * Increments the number of registered turns.
   * Finished battles cannot register new turns.
   */
  public void registerTurn() {
    if (finished) {
      throw new IllegalStateException(
          "Cannot register turns after the battle has finished."
      );
    }

    turnCount++;
  }

  /**
   * Adds an event to the battle log.
   * Events cannot be added after the battle finishes.
   */
  public void addBattleEvent(BattleEvent event) {
    if (finished) {
      throw new IllegalStateException(
          "Cannot add events after the battle has finished."
      );
    }

    battleLog.add(Objects.requireNonNull(
        event, "Battle event cannot be null."
    ));
  }

  /**
   * Finishes the battle and assigns the winner.
   * The winner must be one of the two participants.
   */
  public void finish(Trainer winner) {
    if (finished) {
      throw new IllegalStateException(
          "Battle has already finished."
      );
    }

    Objects.requireNonNull(winner, "Winner cannot be null.");

    if (winner != trainer1 && winner != trainer2) {
      throw new IllegalArgumentException(
          "Winner must be one of the battle participants."
      );
    }

    this.winner = winner;
    this.finished = true;
  }
}
