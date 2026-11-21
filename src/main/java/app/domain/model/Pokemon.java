
package app.domain.model;

import app.domain.effect.StatModifier;
import app.domain.effect.StatusEffect;
import app.domain.enums.ElementalType;
import app.domain.enums.StatType;
import app.domain.enums.StatusType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages a Pokemon's attributes, health, attacks,
 * status conditions and stat modifiers during battles.
 */
public class Pokemon {

  private final String name;
  private final ElementalType type;

  private final int maxHp;
  private final int baseAttack;
  private final int baseDefense;
  private final int baseSpeed;

  private final List<Attack> attacks;
  private final List<StatusEffect> statusEffects;
  private final List<StatModifier> statModifiers;

  private int currentHp;

  /**
   * Creates a Pokemon with its base attributes and attacks.
   * Initializes its HP to the maximum value and starts
   * without status effects or stat modifiers.
   */
  public Pokemon(
      String name,
      ElementalType type,
      int maxHp,
      int baseAttack,
      int baseDefense,
      int baseSpeed,
      List<Attack> attacks
  ) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException(
          "Pokemon name cannot be null or blank."
      );
    }

    if (type == null) {
      throw new IllegalArgumentException(
          "Pokemon type cannot be null."
      );
    }

    if (maxHp <= 0 || baseAttack <= 0
        || baseDefense <= 0 || baseSpeed <= 0) {
      throw new IllegalArgumentException(
          "Pokemon attributes must be greater than zero."
      );
    }

    if (attacks == null || attacks.isEmpty()) {
      throw new IllegalArgumentException(
          "Pokemon must have at least one attack."
      );
    }

    this.name = name;
    this.type = type;
    this.maxHp = maxHp;
    this.currentHp = maxHp;

    this.baseAttack = baseAttack;
    this.baseDefense = baseDefense;
    this.baseSpeed = baseSpeed;

    this.attacks = new ArrayList<>(attacks);
    this.statusEffects = new ArrayList<>();
    this.statModifiers = new ArrayList<>();
  }

  /**
   * Returns the Pokemon's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the Pokemon's elemental type.
   */
  public ElementalType getType() {
    return type;
  }

  /**
   * Returns the maximum HP.
   */
  public int getMaxHp() {
    return maxHp;
  }

  /**
   * Returns the current HP.
   */
  public int getCurrentHp() {
    return currentHp;
  }

  /**
   * Returns the base Attack attribute.
   */
  public int getBaseAttack() {
    return baseAttack;
  }

  /**
   * Returns the base Defense attribute.
   */
  public int getBaseDefense() {
    return baseDefense;
  }

  /**
   * Returns the base Speed attribute.
   */
  public int getBaseSpeed() {
    return baseSpeed;
  }

  /**
   * Returns the Attack attribute after applying
   * all active Attack modifiers.
   */
  public double getEffectiveAttack() {
    return calculateEffectiveStat(StatType.ATTACK, baseAttack);
  }

  /**
   * Returns the Defense attribute after applying
   * all active Defense modifiers.
   */
  public double getEffectiveDefense() {
    return calculateEffectiveStat(StatType.DEFENSE, baseDefense);
  }

  /**
   * Returns the Speed attribute after applying
   * all active Speed modifiers.
   */
  public double getEffectiveSpeed() {
    return calculateEffectiveStat(StatType.SPEED, baseSpeed);
  }

  /**
   * Calculates an effective attribute by multiplying
   * its base value by all matching stat modifiers.
   */
  private double calculateEffectiveStat(
      StatType statType,
      int baseValue
  ) {
    double multiplier = 1.0;

    for (StatModifier modifier : statModifiers) {
      if (modifier.getStatType() == statType) {
        multiplier *= modifier.getMultiplier();
      }
    }

    return baseValue * multiplier;
  }

  /**
   * Returns the available attacks as an unmodifiable list.
   */
  public List<Attack> getAttacks() {
    return Collections.unmodifiableList(attacks);
  }

  /**
   * Returns the active status effects
   * as an unmodifiable list.
   */
  public List<StatusEffect> getStatusEffects() {
    return Collections.unmodifiableList(statusEffects);
  }

  /**
   * Returns the active stat modifiers
   * as an unmodifiable list.
   */
  public List<StatModifier> getStatModifiers() {
    return Collections.unmodifiableList(statModifiers);
  }

  /**
   * Reduces HP by the specified amount without going below zero.
   * Clears status effects and stat modifiers if the Pokemon faints.
   */
  public void takeDamage(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException(
          "Damage cannot be negative."
      );
    }

    currentHp = Math.max(0, currentHp - amount);

    if (!isAlive()) {
      statusEffects.clear();
      statModifiers.clear();
    }
  }

  /**
   * Restores HP without exceeding the maximum value.
   * Fainted Pokemon cannot be healed.
   */
  public void heal(int amount) {
    if (amount < 0) {
      throw new IllegalArgumentException(
          "Healing amount cannot be negative."
      );
    }

    if (isAlive()) {
      currentHp = Math.min(maxHp, currentHp + amount);
    }
  }

  /**
   * Checks whether the Pokemon has remaining HP.
   */
  public boolean isAlive() {
    return currentHp > 0;
  }

  /**
   * Restores the PP of every available attack
   * to its maximum value.
   */
  public void restoreAttacksPP() {
    for (Attack attack : attacks) {
      attack.restorePP();
    }
  }

  /**
   * Adds a status effect to a living Pokemon.
   * Prevents duplicate effects of the same type.
   */
  public void addStatusEffect(StatusEffect effect) {
    if (effect == null) {
      throw new IllegalArgumentException(
          "Status effect cannot be null."
      );
    }

    if (!isAlive()) {
      return;
    }

    boolean alreadyAffected = statusEffects.stream()
        .anyMatch(existing ->
            existing.getType() == effect.getType()
        );

    if (!alreadyAffected) {
      statusEffects.add(effect);
    }
  }

  /**
   * Checks whether the Pokemon has an active
   * status effect of the specified type.
   */
  public boolean hasStatusEffect(StatusType type) {
    return statusEffects.stream()
        .anyMatch(effect -> effect.getType() == type);
  }

  /**
   * Removes all status effects of the specified type.
   */
  public void removeStatusEffect(StatusType type) {
    statusEffects.removeIf(
        effect -> effect.getType() == type
    );
  }

  /**
   * Adds a stat modifier if the Pokemon is alive.
   */
  public void addStatModifier(StatModifier modifier) {
    if (modifier == null) {
      throw new IllegalArgumentException(
          "Stat modifier cannot be null."
      );
    }

    if (isAlive()) {
      statModifiers.add(modifier);
    }
  }

  /**
   * Removes all stat modifiers affecting
   * the specified attribute.
   */
  public void removeStatModifiers(StatType type) {
    statModifiers.removeIf(
        modifier -> modifier.getStatType() == type
    );
  }
}
