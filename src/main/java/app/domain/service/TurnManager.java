
package app.domain.service;

import app.domain.model.Trainer;

import java.util.Objects;

/**
 * Determines which trainer acts first in a battle
 * based on their Pokemon's effective speed.
 */
public class TurnManager {

  /**
   * Compares both Pokemon's effective speed and returns
   * the trainer who acts first.
   * Trainer 1 has priority when speeds are equal.
   */
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
