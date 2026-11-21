
package app.domain.application.battle;

import app.domain.application.port.BattleRegister;
import app.domain.application.port.ResultRepository;
import app.domain.application.port.TerrainService;
import app.domain.effect.ApplyStatModifierEffect;
import app.domain.effect.InflictStatusEffect;
import app.domain.enums.BattleEventType;
import app.domain.enums.ElementalType;
import app.domain.event.BattleEvent;
import app.domain.item.BattleItem;
import app.domain.model.Attack;
import app.domain.model.Battle;
import app.domain.model.BattleResult;
import app.domain.model.Trainer;
import app.domain.port.RandomGenerator;
import app.domain.service.*;
import app.domain.terrain.Arena;
import app.domain.terrain.TerrainContext;
import app.domain.terrain.TerrainEffect;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Coordinates battle execution using domain services.
 * Handles attacks, items, turn progression, battle effects
 * and the storage of completed battle results.
 */
public class BattleService {

  private static final Logger LOGGER =
      Logger.getLogger(BattleService.class.getName());

  private static final int CRITICAL_CHANCE_BOUND = 16;
  private static final int ACCURACY_BOUND = 100;

  private final DamageCalculator damageCalculator;
  private final TurnManager turnManager;
  private final ItemManager itemManager;
  private final StatusEffectManager statusEffectManager;
  private final StatModifierManager statModifierManager;
  private final TypeEffectiveness typeEffectiveness;
  private final TerrainService terrainService;
  private final RandomGenerator randomGenerator;
  private final ResultRepository resultRepository;
  private final BattleRegister battleRegister;

  /**
   * Initializes the services and dependencies required
   * to manage battle execution.
   */
  public BattleService(
      DamageCalculator damageCalculator,
      TurnManager turnManager,
      ItemManager itemManager,
      StatusEffectManager statusEffectManager,
      StatModifierManager statModifierManager,
      TypeEffectiveness typeEffectiveness,
      TerrainService terrainService,
      RandomGenerator randomGenerator,
      BattleRegister battleRegister,
      ResultRepository resultRepository
  ) {
    this.damageCalculator = Objects.requireNonNull(damageCalculator);
    this.turnManager = Objects.requireNonNull(turnManager);
    this.itemManager = Objects.requireNonNull(itemManager);
    this.statusEffectManager = Objects.requireNonNull(statusEffectManager);
    this.statModifierManager = Objects.requireNonNull(statModifierManager);
    this.typeEffectiveness = Objects.requireNonNull(typeEffectiveness);
    this.terrainService = Objects.requireNonNull(terrainService);
    this.randomGenerator = Objects.requireNonNull(randomGenerator);
    this.battleRegister = Objects.requireNonNull(battleRegister);
    this.resultRepository = Objects.requireNonNull(resultRepository);
  }

  /**
   * Prepares a battle by configuring its terrain, restoring
   * attack PP, resetting item usage and determining turn order.
   */
  public void startBattle(Battle battle, Arena arena) {
    Objects.requireNonNull(battle, "Battle cannot be null.");
    Objects.requireNonNull(arena, "Arena cannot be null.");

    if (battle.isStarted() || battle.isFinished()) {
      throw new IllegalStateException(
          "Battle has already started or finished."
      );
    }

    Trainer trainer1 = battle.getTrainer1();
    Trainer trainer2 = battle.getTrainer2();

    if (trainer1.getPokemon() == null || trainer2.getPokemon() == null) {
      throw new IllegalStateException(
          "Both trainers must have a Pokemon."
      );
    }

    TerrainContext context = terrainService.createBattleContext();
    TerrainEffect terrainEffect =
        terrainService.calculateEffect(arena, context);

    trainer1.getPokemon().restoreAttacksPP();
    trainer2.getPokemon().restoreAttacksPP();

    trainer1.resetItemsUsed();
    trainer2.resetItemsUsed();

    Trainer firstTrainer =
        turnManager.determineFirstTrainer(trainer1, trainer2);

    battle.start(firstTrainer, terrainEffect, context);
  }

  /**
   * Executes an attack from the active trainer.
   * Processes accuracy, critical hits, damage and additional
   * effects before completing or ending the battle.
   */
  public void executeAttack(Battle battle, Attack attack) {
    validateActiveBattle(battle);
    Objects.requireNonNull(attack, "Attack cannot be null.");

    Trainer attacker = battle.getCurrentTrainer();
    Trainer defender = getOpponent(battle, attacker);

    if (!attacker.getPokemon().isAlive()) {
      throw new IllegalStateException(
          "The attacking Pokemon has fainted."
      );
    }

    if (!attacker.getPokemon().getAttacks().contains(attack)) {
      throw new IllegalArgumentException(
          "The selected attack does not belong to this Pokemon."
      );
    }

    if (!attack.canBeUsed()) {
      throw new IllegalStateException(
          "The selected attack has no PP remaining."
      );
    }

    attack.consumePP();

    battleRegister.register(new BattleEvent(
        BattleEventType.ATTACK,
        attacker.getPokemon().getName()
            + " used " + attack.getName() + "."
    ));

    if (!hasAttackHit(attack, attacker, battle)) {
      completeTurn(battle, attacker);
      return;
    }

    double typeMultiplier = typeEffectiveness.calculateMultiplier(
        attacker.getPokemon().getType(),
        defender.getPokemon().getType()
    );

    boolean critical = isCriticalHit();

    int damage = damageCalculator.calculateDamage(
        attack,
        attacker.getPokemon(),
        defender.getPokemon(),
        typeMultiplier,
        battle.getTerrainEffect(),
        critical
    );

    defender.getPokemon().takeDamage(damage);

    battleRegister.register(new BattleEvent(
        BattleEventType.DAMAGE,
        attack.getName() + " dealt " + damage + " damage."
    ));

    if (defender.getPokemon().isAlive()) {
      applyAttackEffects(battle, attack, attacker, defender);
    }

    registerTerrainAttackEffect(battle, attacker);

    if (!defender.getPokemon().isAlive()) {
      battle.registerTurn();
      finishBattle(battle, attacker);
      return;
    }

    completeTurn(battle, attacker);
  }

  /**
   * Uses an item on the active trainer's Pokemon
   * and completes the current turn.
   */
  public void useItem(Battle battle, BattleItem item) {
    validateActiveBattle(battle);
    Objects.requireNonNull(item, "Item cannot be null.");

    Trainer trainer = battle.getCurrentTrainer();

    if (!trainer.getPokemon().isAlive()) {
      throw new IllegalStateException(
          "A fainted Pokemon cannot use an item."
      );
    }

    itemManager.useItem(trainer, item);

    battleRegister.register(new BattleEvent(
        BattleEventType.ITEM_USE,
        trainer.getPokemon().getName()
            + " used " + item.getName() + "."
    ));

    completeTurn(battle, trainer);
  }

  /**
   * Checks whether an attack hits its target.
   * Applies the terrain accuracy bonus to Water Pokemon.
   */
  private boolean hasAttackHit(
      Attack attack,
      Trainer attacker,
      Battle battle
  ) {
    double accuracy = attack.getAccuracy();

    if (attacker.getPokemon().getType() == ElementalType.WATER) {
      int bonus = (int) Math.round(
          battle.getTerrainEffect().getWaterAccuracyBonus()
              * ACCURACY_BOUND
      );

      accuracy = Math.min(ACCURACY_BOUND, accuracy + bonus);
    }

    return randomGenerator.nextInt(ACCURACY_BOUND) < accuracy;
  }

  /**
   * Determines whether an attack is critical
   * using the configured random generator.
   */
  private boolean isCriticalHit() {
    return randomGenerator.nextInt(CRITICAL_CHANCE_BOUND) == 0;
  }

  /**
   * Applies additional attack effects to the defending Pokemon.
   * Handles status conditions and stat modifiers,
   * registering an event for each applied effect.
   */
  private void applyAttackEffects(
      Battle battle,
      Attack attack,
      Trainer attacker,
      Trainer defender
  ) {
    for (Object effect : attack.getEffects()) {
      if (effect instanceof InflictStatusEffect statusEffect) {
        var status = statusEffect.getStatusEffect();

        if (!defender.getPokemon().hasStatusEffect(status.getType())) {
          statusEffectManager.applyStatusEffect(
              defender.getPokemon(),
              status
          );

          battleRegister.register(new BattleEvent(
              BattleEventType.STATUS_EFFECT,
              defender.getPokemon().getName()
                  + " received "
                  + status.getType() + "."
          ));
        }
      }

      if (effect instanceof ApplyStatModifierEffect modifierEffect) {
        statModifierManager.applyModifier(
            defender.getPokemon(),
            modifierEffect.getStatModifier()
        );

        battleRegister.register(new BattleEvent(
            BattleEventType.STAT_MODIFIER,
            defender.getPokemon().getName()
                + " received a stat modifier."
        ));
      }
    }
  }

  /**
   * Registers an event when the attacking Pokemon's
   * elemental type receives a terrain bonus.
   */
  private void registerTerrainAttackEffect(
      Battle battle,
      Trainer attacker
  ) {
    TerrainEffect effect = battle.getTerrainEffect();
    ElementalType type = attacker.getPokemon().getType();

    boolean terrainAffectedAttack =
        (type == ElementalType.FIRE
            && effect.getFireDamageMultiplier() != 1.0)
            || (type == ElementalType.WATER
            && (effect.getWaterDamageMultiplier() != 1.0
            || effect.getWaterAccuracyBonus() > 0.0));

    if (terrainAffectedAttack) {
      battleRegister.register(new BattleEvent(
          BattleEventType.TERRAIN_EFFECT,
          "The terrain affected the attack."
      ));
    }
  }

  /**
   * Processes end-of-turn effects and registers the turn.
   * Finishes the battle if the active Pokemon faints;
   * otherwise, passes control to the opposing trainer.
   */
  private void completeTurn(Battle battle, Trainer trainer) {
    processEndOfTurnEffects(battle, trainer);

    battle.registerTurn();

    if (!trainer.getPokemon().isAlive()) {
      finishBattle(battle, getOpponent(battle, trainer));
      return;
    }

    battle.advanceTurn();
  }

  /**
   * Processes status damage and terrain healing
   * for the Pokemon completing its turn.
   */
  private void processEndOfTurnEffects(
      Battle battle,
      Trainer trainer
  ) {
    var pokemon = trainer.getPokemon();

    statusEffectManager.processEndOfTurn(pokemon);

    if (!pokemon.isAlive()) {
      return;
    }

    double healingPercentage =
        battle.getTerrainEffect().getGrassHealingPercentage();

    if (pokemon.getType() == ElementalType.GRASS
        && healingPercentage > 0.0) {

      int healing = (int) Math.ceil(
          pokemon.getMaxHp() * healingPercentage
      );

      int previousHp = pokemon.getCurrentHp();

      pokemon.heal(healing);

      if (pokemon.getCurrentHp() > previousHp) {
        battleRegister.register(new BattleEvent(
            BattleEventType.TERRAIN_EFFECT,
            pokemon.getName()
                + " recovered HP from the terrain."
        ));
      }
    }
  }

  /**
   * Finishes the battle and attempts to save its result.
   * Repository failures are logged without undoing
   * the completed battle.
   */
  private void finishBattle(Battle battle, Trainer winner) {
    battle.finish(winner);

    BattleResult result = new BattleResult(battle);

    try {
      resultRepository.save(result);
    } catch (RuntimeException exception) {
      LOGGER.log(
          Level.SEVERE,
          "Battle finished, but its result could not be saved.",
          exception
      );
    }
  }

  /**
   * Returns the opposing trainer in the battle.
   */
  private Trainer getOpponent(Battle battle, Trainer trainer) {
    return battle.getTrainer1() == trainer
        ? battle.getTrainer2()
        : battle.getTrainer1();
  }

  /**
   * Validates that a battle has started
   * and has not already finished.
   */
  private void validateActiveBattle(Battle battle) {
    Objects.requireNonNull(battle, "Battle cannot be null.");

    if (!battle.isStarted() || battle.isFinished()) {
      throw new IllegalStateException(
          "Battle is not active."
      );
    }
  }
}
