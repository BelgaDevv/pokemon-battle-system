package app.domain.service;

import app.domain.model.Trainer;

import java.util.Objects;

/**
 * Determines the initial turn order for a Pokemon battle.
 *
 * Compares the effective speed of each trainer's Pokemon
 * to decide which trainer acts first. Trainer 1 wins
 * speed ties.
 */
public class TurnManager {

  public Trainer determineFirstTrainer(
      Trainer trainer1,
      Trainer trainer2
  ) {
    Objects.requireNonNull(trainer1, "Trainer 1 cannot be null.");
    Objects.requireNonNull(trainer2, "Trainer 2 cannot be null.");

    if (trainer1.getPokemon() == null || trainer2.getPokemon() == null) {
      throw new IllegalStateException(
          "Both trainers must have a Pokemon before battle."
      );
    }

    double trainer1Speed = trainer1.getPokemon().getEffectiveSpeed();
    double trainer2Speed = trainer2.getPokemon().getEffectiveSpeed();

    return trainer1Speed >= trainer2Speed
        ? trainer1
        : trainer2;
  }
}