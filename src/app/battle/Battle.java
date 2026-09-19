package app.battle;

import app.trainer.Trainer;
import app.arena.Arena;

/*
operate the battles fluxes
 */
public class Battle {

    private Trainer trainer1;
    private Trainer trainer2;
    private Arena arena;
    private Trainer currentTrainer;
    private int round;
    private Trainer Winner;

    // class constructor
    public Battle(Arena arena, Trainer currentTrainer, int round, Trainer trainer1,
                  Trainer trainer2, Trainer winner) {

        this.arena = arena;
        this.currentTrainer = currentTrainer;
        this.round = round;
        this.trainer1 = trainer1;
        this.trainer2 = trainer2;
        Winner = winner;
    }

    public Arena getArena() {
        return arena;
    }

    public Trainer getCurrentTrainer() {
        return currentTrainer;
    }

    public int getRound() {
        return round;
    }

    public Trainer getTrainer1() {
        return trainer1;
    }

    public Trainer getTrainer2() {
        return trainer2;
    }

    public Trainer getWinner() {
        return Winner;
    }
}
