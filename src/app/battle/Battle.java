package app.battle;

import app.arena.Arena;
import app.attack.Attack;
import app.item.Item;
import app.pokemon.Pokemon;
import app.trainer.Trainer;

/**
 * Operates the battle flow.
 */
public class Battle {

  private Trainer trainer1;
  private Trainer trainer2;
  private Arena arena;
  private Trainer currentTrainer;
  private int round;
  private Trainer winner;

  /**
   * Creates a battle between two trainers in the specified arena.
   */
  public Battle(Arena arena, Trainer trainer1, Trainer trainer2) {

    if (arena == null || trainer1 == null || trainer2 == null) {
      throw new IllegalArgumentException(
          "Arena and trainers cannot be null.");
    }

    this.arena = arena;
    this.trainer1 = trainer1;
    this.trainer2 = trainer2;
    this.round = 1;
    this.winner = null;

    if (trainer1.getPokemon().getSpe()
        >= trainer2.getPokemon().getSpe()) {
      this.currentTrainer = trainer1;
    } else {
      this.currentTrainer = trainer2;
    }
  }

  /**
   * Executes an attack for the current trainer.
   */
  public void executeAttack(Attack attack) {

    if (winner != null) {
      throw new IllegalStateException("Battle has already ended.");
    }

    if (attack == null) {
      throw new IllegalArgumentException("Attack cannot be null.");
    }

    Pokemon attacker = currentTrainer.getPokemon();
    Trainer opponentTrainer = getOpponent(currentTrainer);
    Pokemon defender = opponentTrainer.getPokemon();

    if (!belongsToPokemon(attack, attacker)) {
      throw new IllegalArgumentException(
          "Attack does not belong to the current Pokemon.");
    }

    if (!attack.canUse()) {
      throw new IllegalStateException(
          "Attack has no PP remaining.");
    }

    boolean attackerWasFaster =
        attacker.getSpe() >= defender.getSpe();

    attack.use();

    double damage = calculateDamage(attack, attacker, defender);

    defender.setHpCurrent(
        defender.getHpCurrent() - damage
    );

    applyStatus(attack, defender);

    checkWinner();

    if (winner == null) {
      endTurn(attackerWasFaster);
    }
  }

  /**
   * Calculates the damage dealt by an attack.
   */
  public double calculateDamage(
      Attack attack,
      Pokemon attacker,
      Pokemon defender) {

    if (defender.getDef() <= 0) {
      throw new IllegalArgumentException(
          "Defense must be greater than zero.");
    }

    double damage =
        (attack.getDamage() * attacker.getAtk())
            / defender.getDef();

    damage *= attack.getType()
        .getDamageMultiplier(defender.getType());

    damage = arena.getArenaEffect()
        .fireDamageBonus(damage, attack);

    damage = arena.getArenaEffect()
        .waterDamageBonus(damage, attack);

    return Math.max(damage, 0);
  }

  /**
   * Applies the status effect associated with an attack.
   */
  private void applyStatus(Attack attack, Pokemon defender) {

    if (attack.getStatus() == null
        || defender.getHpCurrent() <= 0) {
      return;
    }

    defender.setStatus(attack.getStatus());

    String statusName = attack.getStatus().getName();

    if ("Burn".equals(statusName)) {

      attack.getStatus()
          .getStatusEffect()
          .applyBurn(defender);

    } else if ("Paralysis".equals(statusName)) {

      attack.getStatus()
          .getStatusEffect()
          .applyParalysis(defender);
    }
  }

  /**
   * Uses an item for the current trainer.
   */
  public void useItem(Item item) {

    if (winner != null) {
      throw new IllegalStateException("Battle has already ended.");
    }

    if (item == null) {
      throw new IllegalArgumentException("Item cannot be null.");
    }

    if (!currentTrainer.useItem()) {
      throw new IllegalStateException(
          "Trainer has reached the item limit.");
    }

    Pokemon pokemon = currentTrainer.getPokemon();

    if ("Potion".equals(item.getName())) {

      item.getItemEffect().heal(pokemon);

    } else if ("Burn Heal".equals(item.getName())) {

      item.getItemEffect().clearBurn(pokemon);

    } else if ("Antidote".equals(item.getName())) {

      item.getItemEffect().clearPoison(pokemon);

    } else if ("Paralyze Heal".equals(item.getName())) {

      item.getItemEffect().clearParalysis(pokemon);

    } else {
      throw new IllegalArgumentException(
          "Unknown item.");
    }

    boolean trainerWasFaster =
        currentTrainer.getPokemon().getSpe()
            >= getOpponent(currentTrainer)
            .getPokemon().getSpe();

    endTurn(trainerWasFaster);
  }

  /**
   * Ends the current turn and processes its effects.
   */
  private void endTurn(boolean trainerWasFaster) {

    processStatusEffects();

    if (winner != null) {
      return;
    }

    arena.getArenaEffect()
        .plantRecoverBonus(trainer1.getPokemon());

    arena.getArenaEffect()
        .plantRecoverBonus(trainer2.getPokemon());

    checkWinner();

    if (winner != null) {
      return;
    }

    round++;

    nextTurn(trainerWasFaster);
  }

  /**
   * Processes the status effects of both Pokemon.
   */
  private void processStatusEffects() {

    processStatus(trainer1.getPokemon());
    processStatus(trainer2.getPokemon());

    checkWinner();
  }

  /**
   * Processes the status effect of a Pokemon.
   */
  private void processStatus(Pokemon pokemon) {

    if (pokemon.getStatus() == null) {
      return;
    }

    String statusName = pokemon.getStatus().getName();

    if ("Burn".equals(statusName)) {

      pokemon.getStatus()
          .getStatusEffect()
          .processBurn(pokemon);

    } else if ("Poison".equals(statusName)) {

      pokemon.getStatus()
          .getStatusEffect()
          .processPoison(pokemon);
    }
  }

  /**
   * Determines which trainer acts next based on speed.
   * <p>
   * The faster trainer acts first. After the faster trainer acts,
   * the opponent gets the turn.
   */
  private void nextTurn(boolean currentWasFaster) {

    Trainer opponent = getOpponent(currentTrainer);

    if (currentWasFaster) {
      currentTrainer = opponent;
      return;
    }

    if (currentTrainer.getPokemon().getSpe()
        >= opponent.getPokemon().getSpe()) {
      return;
    }

    currentTrainer = opponent;
  }

  /**
   * Checks whether one of the trainers has won the battle.
   */
  private void checkWinner() {

    if (trainer1.getPokemon().getHpCurrent() <= 0) {
      winner = trainer2;

    } else if (trainer2.getPokemon().getHpCurrent() <= 0) {
      winner = trainer1;
    }
  }

  /**
   * Returns the opponent of the specified trainer.
   */
  private Trainer getOpponent(Trainer trainer) {

    if (trainer == trainer1) {
      return trainer2;
    }

    return trainer1;
  }

  /**
   * Checks whether an attack belongs to the specified Pokemon.
   */
  private boolean belongsToPokemon(
      Attack attack,
      Pokemon pokemon) {

    for (Attack pokemonAttack : pokemon.getAttack()) {

      if (pokemonAttack == attack) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns the arena where the battle takes place.
   */
  public Arena getArena() {
    return arena;
  }

  /**
   * Returns the trainer whose turn it currently is.
   */
  public Trainer getCurrentTrainer() {
    return currentTrainer;
  }

  /**
   * Returns the current round number.
   */
  public int getRound() {
    return round;
  }

  /**
   * Returns the first trainer.
   */
  public Trainer getTrainer1() {
    return trainer1;
  }

  /**
   * Returns the second trainer.
   */
  public Trainer getTrainer2() {
    return trainer2;
  }

  /**
   * Returns the winner of the battle.
   */
  public Trainer getWinner() {
    return winner;
  }
}