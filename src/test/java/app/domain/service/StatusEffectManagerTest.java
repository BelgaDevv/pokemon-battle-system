
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

    Attack attack = new Attack("Tackle", 30, 100, 10, List.of());

    pokemon = new Pokemon(
        "CharSal", ElementalType.FIRE,
        100, 50, 40, 30, List.of(attack)
    );
  }

  // Applies burn and reduces effective attack.
  @Test
  void shouldApplyBurnEffect() {
    statusEffectManager.applyStatusEffect(pokemon, new BurnEffect());

    assertTrue(pokemon.hasStatusEffect(StatusType.BURN));
    assertEquals(40.0, pokemon.getEffectiveAttack());
  }

  // Applies poison to the Pokemon.
  @Test
  void shouldApplyPoisonEffect() {
    statusEffectManager.applyStatusEffect(pokemon, new PoisonEffect());

    assertTrue(pokemon.hasStatusEffect(StatusType.POISON));
  }

  // Applies paralysis and reduces effective speed.
  @Test
  void shouldApplyParalysisEffect() {
    statusEffectManager.applyStatusEffect(pokemon, new ParalysisEffect());

    assertTrue(pokemon.hasStatusEffect(StatusType.PARALYSIS));
    assertEquals(15.0, pokemon.getEffectiveSpeed());
  }

  // Prevents applying the same status more than once.
  @Test
  void shouldNotApplyDuplicateStatusEffect() {
    statusEffectManager.applyStatusEffect(pokemon, new BurnEffect());
    statusEffectManager.applyStatusEffect(pokemon, new BurnEffect());

    assertEquals(40.0, pokemon.getEffectiveAttack());
  }

  // Applies burn damage at the end of the turn.
  @Test
  void shouldApplyBurnDamageAtEndOfTurn() {
    statusEffectManager.applyStatusEffect(pokemon, new BurnEffect());

    statusEffectManager.processEndOfTurn(pokemon);

    assertEquals(95, pokemon.getCurrentHp());
  }


  // Applies increasing poison damage over consecutive turns.
  @Test
  void shouldIncreasePoisonDamageEachTurn() {
    statusEffectManager.applyStatusEffect(pokemon, new PoisonEffect());

    assertEquals(100, pokemon.getCurrentHp());

    statusEffectManager.processEndOfTurn(pokemon);
    assertEquals(95, pokemon.getCurrentHp());

    statusEffectManager.processEndOfTurn(pokemon);
    assertEquals(87, pokemon.getCurrentHp());

    statusEffectManager.processEndOfTurn(pokemon);
    assertEquals(78, pokemon.getCurrentHp());
  }



  // Caps poison damage at fifteen percent of maximum HP.
  @Test
  void shouldCapPoisonDamageAtFifteenPercent() {
    Attack attack = new Attack("Tackle", 30, 100, 10, List.of());

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

  // Does not damage Pokemon affected only by paralysis.
  @Test
  void shouldNotDamagePokemonWithOnlyParalysis() {
    statusEffectManager.applyStatusEffect(pokemon, new ParalysisEffect());

    statusEffectManager.processEndOfTurn(pokemon);

    assertEquals(100, pokemon.getCurrentHp());
  }

  // Stops processing poison after burn causes fainting.
  @Test
  void shouldStopProcessingAfterPokemonFaints() {
    pokemon.takeDamage(95);

    statusEffectManager.applyStatusEffect(pokemon, new BurnEffect());
    statusEffectManager.applyStatusEffect(pokemon, new PoisonEffect());

    statusEffectManager.processEndOfTurn(pokemon);

    assertEquals(0, pokemon.getCurrentHp());
    assertFalse(pokemon.hasStatusEffect(StatusType.BURN));
    assertFalse(pokemon.hasStatusEffect(StatusType.POISON));
  }

  // Removes poison tracking when the status is removed.
  @Test
  void shouldStopPoisonDamageAfterStatusRemoval() {
    statusEffectManager.applyStatusEffect(pokemon, new PoisonEffect());

    statusEffectManager.processEndOfTurn(pokemon);
    pokemon.removeStatusEffect(StatusType.POISON);

    statusEffectManager.processEndOfTurn(pokemon);

    assertEquals(95, pokemon.getCurrentHp());
  }

  // Rejects a null Pokemon.
  @Test
  void shouldRejectNullPokemon() {
    assertThrows(
        NullPointerException.class,
        () -> statusEffectManager.applyStatusEffect(null, new BurnEffect())
    );
  }

  // Rejects a null status effect.
  @Test
  void shouldRejectNullStatusEffect() {
    assertThrows(
        NullPointerException.class,
        () -> statusEffectManager.applyStatusEffect(pokemon, null)
    );
  }

  // Rejects a null Pokemon when processing the turn.
  @Test
  void shouldRejectNullPokemonWhenProcessingTurn() {
    assertThrows(
        NullPointerException.class,
        () -> statusEffectManager.processEndOfTurn(null)
    );
  }
}
