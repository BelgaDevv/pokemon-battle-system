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
 * Represents a Pokemon participating in a battle.
 *
 * Each instance maintains its own health, attacks, status effects
 * and stat modifiers.
 */
public class Pokemon {

  private final String name;
  private final ElementalType type;

  private final int maxHp;
  private int currentHp;

  private final int baseAttack;
  private final int baseDefense;
  private final int baseSpeed;

  private final List<Attack> attacks;
  private final List<StatusEffect> statusEffects;
  private final List<StatModifier> statModifiers;

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

  public String getName() {
    return name;
  }

  public ElementalType getType() {
    return type;
  }

  public int getMaxHp() {
    return maxHp;
  }

  public int getCurrentHp() {
    return currentHp;
  }

  public int getBaseAttack() {
    return baseAttack;
  }

  public int getBaseDefense() {
    return baseDefense;
  }

  public int getBaseSpeed() {
    return baseSpeed;
  }

  public double getEffectiveAttack() {
    return calculateEffectiveStat(StatType.ATTACK, baseAttack);
  }

  public double getEffectiveDefense() {
    return calculateEffectiveStat(StatType.DEFENSE, baseDefense);
  }

  public double getEffectiveSpeed() {
    return calculateEffectiveStat(StatType.SPEED, baseSpeed);
  }

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

  public List<Attack> getAttacks() {
    return Collections.unmodifiableList(attacks);
  }

  public List<StatusEffect> getStatusEffects() {
    return Collections.unmodifiableList(statusEffects);
  }

  public List<StatModifier> getStatModifiers() {
    return Collections.unmodifiableList(statModifiers);
  }

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

  public boolean isAlive() {
    return currentHp > 0;
  }

  public void restoreAttacksPP() {
    for (Attack attack : attacks) {
      attack.restorePP();
    }
  }

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

  public boolean hasStatusEffect(StatusType type) {
    return statusEffects.stream()
        .anyMatch(effect -> effect.getType() == type);
  }

  public void removeStatusEffect(StatusType type) {
    statusEffects.removeIf(
        effect -> effect.getType() == type
    );
  }

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

  public void removeStatModifiers(StatType type) {
    statModifiers.removeIf(
        modifier -> modifier.getStatType() == type
    );
  }
}