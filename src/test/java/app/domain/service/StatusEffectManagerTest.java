
package app.domain.service;

import app.domain.effect.BurnEffect;
import app.domain.effect.ParalysisEffect;
import app.domain.effect.PoisonEffect;
import app.domain.enums.ElementalType;
import app.domain.enums.StatusType;
import app.domain.model.Attack;
import app.domain.model.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatusEffectManagerTest {

  private StatusEffectManager statusEffectManager;
  private Pokemon pokemon;

  @BeforeEach
  void setUp() {
    statusEffectManager = new StatusEffectManager();

    Attack attack = new Attack(
        "Tackle", 30, 100, 10, List.of()
    );

    pokemon = new Pokemon(
        "CharSal", ElementalType.FIRE,
        100, 50, 40, 30, List.of(attack)
    );
  }

  // Applies burn and reduces effective attack.
  @Test
  void shouldApplyBurnEffect() {
    statusEffectManager.applyStatusEffect(
        pokemon, new BurnEffect()
    );

    assertTrue(pokemon.hasStatusEffect(StatusType.BURN));
    assertEquals(40.0, pokemon.getEffectiveAttack());
  }

  // Applies paralysis and reduces effective speed.
  @Test
  void shouldApplyParalysisEffect() {
    statusEffectManager.applyStatusEffect(
        pokemon, new ParalysisEffect()
    );

    assertTrue(pokemon.hasStatusEffect(StatusType.PARALYSIS));
    assertEquals(15.0, pokemon.getEffectiveSpeed());
  }

  // Prevents applying the same status more than once.
  @Test
  void shouldNotApplyDuplicateStatusEffect() {
    statusEffectManager.applyStatusEffect(
        pokemon, new BurnEffect()
    );

    statusEffectManager.applyStatusEffect(
        pokemon, new BurnEffect()
    );

    assertEquals(40.0, pokemon.getEffectiveAttack());
  }

  // Applies burn damage at the end of the turn.
  @Test
  void shouldApplyBurnDamageAtEndOfTurn() {
    statusEffectManager.applyStatusEffect(
        pokemon, new BurnEffect()
    );

    statusEffectManager.processEndOfTurn(pokemon);

    assertEquals(95, pokemon.getCurrentHp());
  }

  // Applies increasing poison damage over consecutive turns.
  @Test
  void shouldIncreasePoisonDamageEachTurn() {
    statusEffectManager.applyStatusEffect(
        pokemon, new PoisonEffect()
    );

    assertTrue(pokemon.hasStatusEffect(StatusType.POISON));
    assertEquals(100, pokemon.getCurrentHp());

    statusEffectManager.processEndOfTurn(pokemon);
    assertEquals(95, pokemon.getCurrentHp());

    statusEffectManager.processEndOfTurn(pokemon);
    assertEquals(87, pokemon.getCurrentHp());

    statusEffectManager.processEndOfTurn(pokemon);
    assertEquals(78, pokemon.getCurrentHp());
  }

  // Limits poison damage to fifteen percent of maximum HP.
  @Test
  void shouldCapPoisonDamageAtFifteenPercent() {
    Attack attack = new Attack(
        "Tackle", 30, 100, 10, List.of()
    );

    Pokemon highHpPokemon = new Pokemon(
        "BulbaSal",
        ElementalType.GRASS,
        1000, 50, 40, 30,
        List.of(attack)
    );

    statusEffectManager.applyStatusEffect(
        highHpPokemon,
        new PoisonEffect()
    );

    for (int i = 0; i < 7; i++) {
      statusEffectManager.processEndOfTurn(highHpPokemon);
    }

    assertEquals(250, highHpPokemon.getCurrentHp());
  }

  // Prevents paralysis from causing end-of-turn damage.
  @Test
  void shouldNotDamagePokemonWithOnlyParalysis() {
    statusEffectManager.applyStatusEffect(
        pokemon, new ParalysisEffect()
    );

    statusEffectManager.processEndOfTurn(pokemon);

    assertEquals(100, pokemon.getCurrentHp());
  }

  // Stops processing status effects after the Pokemon faints.
  @Test
  void shouldStopProcessingAfterPokemonFaints() {
    pokemon.takeDamage(95);

    statusEffectManager.applyStatusEffect(
        pokemon, new BurnEffect()
    );

    statusEffectManager.applyStatusEffect(
        pokemon, new PoisonEffect()
    );

    statusEffectManager.processEndOfTurn(pokemon);

    assertEquals(0, pokemon.getCurrentHp());
    assertFalse(pokemon.hasStatusEffect(StatusType.BURN));
    assertFalse(pokemon.hasStatusEffect(StatusType.POISON));
  }

  // Stops poison damage after the status is removed.
  @Test
  void shouldStopPoisonDamageAfterStatusRemoval() {
    statusEffectManager.applyStatusEffect(
        pokemon, new PoisonEffect()
    );

    statusEffectManager.processEndOfTurn(pokemon);
    pokemon.removeStatusEffect(StatusType.POISON);

    statusEffectManager.processEndOfTurn(pokemon);

    assertEquals(95, pokemon.getCurrentHp());
  }
}
