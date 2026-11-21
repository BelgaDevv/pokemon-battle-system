
package app.domain.application.catalog;

import app.domain.effect.*;
import app.domain.enums.ElementalType;
import app.domain.enums.StatType;
import app.domain.model.Attack;
import app.domain.model.Pokemon;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the available Pokemon species and creates
 * independent instances with their attributes and attacks.
 */
public class PokemonCatalog {

  private static final String CHARMANDER = "Charmander";
  private static final String CYNDAQUIL = "Cyndaquil";
  private static final String SQUIRTLE = "Squirtle";
  private static final String TOTODILE = "Totodile";
  private static final String BULBASAUR = "Bulbasaur";
  private static final String CHIKORITA = "Chikorita";

  /**
   * Creates a new instance of each available Pokemon species.
   *
   * @return a list containing the six available Pokemon
   */
  public List<Pokemon> getAvailablePokemon() {
    return List.of(
        createPokemon(CHARMANDER),
        createPokemon(CYNDAQUIL),
        createPokemon(SQUIRTLE),
        createPokemon(TOTODILE),
        createPokemon(BULBASAUR),
        createPokemon(CHIKORITA)
    );
  }

  /**
   * Creates a new Pokemon based on its species name.
   * Accepts names regardless of letter case or surrounding spaces.
   *
   * @param name the Pokemon species name
   * @return a new Pokemon instance
   * @throws IllegalArgumentException if the name is null, blank or unknown
   */
  public Pokemon createPokemon(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException(
          "Pokemon name cannot be null or blank."
      );
    }

    return switch (name.trim().toLowerCase()) {
      case "charmander" -> createCharmander();
      case "cyndaquil" -> createCyndaquil();
      case "squirtle" -> createSquirtle();
      case "totodile" -> createTotodile();
      case "bulbasaur" -> createBulbasaur();
      case "chikorita" -> createChikorita();
      default -> throw new IllegalArgumentException(
          "Unknown Pokemon species: " + name
      );
    };
  }

  /**
   * Creates a Charmander with Fire attacks
   * and an attack that can inflict burn.
   */
  private Pokemon createCharmander() {
    return new Pokemon(
        CHARMANDER,
        ElementalType.FIRE,
        90, 52, 40, 58,
        List.of(
            createAttack("Ember", 40, 80.0, 15),
            createStatusAttack(
                "Flame Burst", 30, 80.0, 10,
                new BurnEffect()
            )
        )
    );
  }

  /**
   * Creates a Cyndaquil with Fire attacks
   * and an attack that applies an Attack modifier.
   */
  private Pokemon createCyndaquil() {
    return new Pokemon(
        CYNDAQUIL,
        ElementalType.FIRE,
        85, 48, 42, 62,
        List.of(
            createAttack("Quick Flame", 35, 90.00, 20),
            createModifierAttack(
                "Burning Charge", 30, 80.0, 10,
                StatType.ATTACK, 1.20
            )
        )
    );
  }

  /**
   * Creates a Squirtle with Water attacks
   * and an attack that applies a Defense modifier.
   */
  private Pokemon createSquirtle() {
    return new Pokemon(
        SQUIRTLE,
        ElementalType.WATER,
        105, 42, 58, 38,
        List.of(
            createAttack("Water Gun", 40, 80.0, 15),
            createModifierAttack(
                "Aqua Shield", 25, 80.0, 10,
                StatType.DEFENSE, 1.20
            )
        )
    );
  }

  /**
   * Creates a Totodile with Water attacks
   * and an attack that reduces Defense.
   */
  private Pokemon createTotodile() {
    return new Pokemon(
        TOTODILE,
        ElementalType.WATER,
        100, 55, 50, 43,
        List.of(
            createAttack("Water Pulse", 45, 80.0, 15),
            createModifierAttack(
                "Crushing Bite", 35, 80.0, 10,
                StatType.DEFENSE, 0.80
            )
        )
    );
  }

  /**
   * Creates a Bulbasaur with Grass attacks
   * and an attack that can inflict poison.
   */
  private Pokemon createBulbasaur() {
    return new Pokemon(
        BULBASAUR,
        ElementalType.GRASS,
        100, 45, 52, 42,
        List.of(
            createAttack("Vine Whip", 40, 80.0, 15),
            createStatusAttack(
                "Poison Powder", 20, 80.0, 10,
                new PoisonEffect()
            )
        )
    );
  }

  /**
   * Creates a Chikorita with Grass attacks
   * and an attack that can inflict paralysis.
   */
  private Pokemon createChikorita() {
    return new Pokemon(
        CHIKORITA,
        ElementalType.GRASS,
        110, 40, 58, 38,
        List.of(
            createAttack("Razor Leaf", 40, 80.0, 15),
            createStatusAttack(
                "Paralysis Leaf", 25, 80.0, 10,
                new ParalysisEffect()
            )
        )
    );
  }

  /**
   * Creates an attack that deals damage
   * without additional effects.
   */
  private Attack createAttack(
      String name,
      int power,
      double accuracy,
      int maxPP
  ) {
    return new Attack(name, power, accuracy, maxPP, List.of());
  }

  /**
   * Creates an attack that deals damage
   * and applies a status condition.
   */
  private Attack createStatusAttack(
      String name,
      int power,
      double accuracy,
      int maxPP,
      StatusEffect statusEffect
  ) {
    List<AttackEffect> effects = new ArrayList<>();
    effects.add(new InflictStatusEffect(statusEffect));

    return new Attack(name, power, accuracy, maxPP, effects);
  }

  /**
   * Creates an attack that deals damage
   * and applies a stat modifier.
   */
  private Attack createModifierAttack(
      String name,
      int power,
      double accuracy,
      int maxPP,
      StatType statType,
      double multiplier
  ) {
    List<AttackEffect> effects = new ArrayList<>();
    effects.add(new ApplyStatModifierEffect(
        new StatModifier(statType, multiplier)
    ));

    return new Attack(name, power, accuracy, maxPP, effects);
  }
}
