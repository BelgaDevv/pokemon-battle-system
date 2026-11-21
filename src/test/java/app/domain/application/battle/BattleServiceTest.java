package app.domain.application.battle;

import app.domain.application.battle.BattleService;
import app.domain.application.port.BattleRegister;
import app.domain.application.port.ResultRepository;
import app.domain.application.port.TerrainService;
import app.domain.effect.BurnEffect;
import app.domain.effect.InflictStatusEffect;
import app.domain.enums.BattleEventType;
import app.domain.enums.ElementalType;
import app.domain.event.BattleEvent;
import app.domain.model.*;
import app.domain.port.RandomGenerator;
import app.domain.service.*;
import app.domain.terrain.Arena;
import app.domain.terrain.TerrainContext;
import app.domain.terrain.TerrainEffect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BattleServiceTest {

  @Mock
  private DamageCalculator damageCalculator;
  @Mock
  private ItemManager itemManager;

  @Mock
  private BattleRegister battleRegister;

  @Mock
  private TurnManager turnManager;

  @Mock
  private StatusEffectManager statusEffectManager;

  @Mock
  private StatModifierManager statModifierManager;

  @Mock
  private TypeEffectiveness typeEffectiveness;

  @Mock
  private TerrainService terrainService;

  @Mock
  private RandomGenerator randomGenerator;

  @Mock
  private ResultRepository resultRepository;

  @Mock
  private Battle battle;

  @Mock
  private Trainer trainer1;

  @Mock
  private Trainer trainer2;

  @Mock
  private Pokemon pokemon1;

  @Mock
  private Pokemon pokemon2;

  @Mock
  private Arena arena;

  @Mock
  private TerrainContext terrainContext;

  @Mock
  private TerrainEffect terrainEffect;

  @InjectMocks
  private BattleService battleService;

  // Initializes the battle and its dependencies.
  @Test
  void shouldStartBattleAndRestoreResources() {
    when(battle.isStarted()).thenReturn(false);
    when(battle.isFinished()).thenReturn(false);

    when(battle.getTrainer1()).thenReturn(trainer1);
    when(battle.getTrainer2()).thenReturn(trainer2);

    when(trainer1.getPokemon()).thenReturn(pokemon1);
    when(trainer2.getPokemon()).thenReturn(pokemon2);

    when(terrainService.createBattleContext())
        .thenReturn(terrainContext);

    when(terrainService.calculateEffect(arena, terrainContext))
        .thenReturn(terrainEffect);

    when(turnManager.determineFirstTrainer(trainer1, trainer2))
        .thenReturn(trainer1);

    battleService.startBattle(battle, arena);

    verify(pokemon1).restoreAttacksPP();
    verify(pokemon2).restoreAttacksPP();

    verify(trainer1).resetItemsUsed();
    verify(trainer2).resetItemsUsed();

    verify(terrainService).createBattleContext();
    verify(terrainService).calculateEffect(arena, terrainContext);

    verify(battle).start(trainer1, terrainEffect, terrainContext);
  }

  // Executes an attack and applies its damage.
  @Test
  void shouldExecuteAttackSuccessfully() {
    Attack attack = new Attack(
        "Flamethrower",
        40,
        100,
        10,
        List.of()
    );

    when(battle.isStarted()).thenReturn(true);
    when(battle.isFinished()).thenReturn(false);

    when(battle.getCurrentTrainer()).thenReturn(trainer1);
    when(battle.getTrainer1()).thenReturn(trainer1);
    when(battle.getTrainer2()).thenReturn(trainer2);

    when(trainer1.getPokemon()).thenReturn(pokemon1);
    when(trainer2.getPokemon()).thenReturn(pokemon2);

    when(pokemon1.isAlive()).thenReturn(true);
    when(pokemon2.isAlive()).thenReturn(true);

    when(pokemon1.getName()).thenReturn("CharSal");
    when(pokemon1.getType()).thenReturn(ElementalType.FIRE);
    when(pokemon2.getType()).thenReturn(ElementalType.GRASS);

    when(pokemon1.getAttacks()).thenReturn(List.of(attack));

    when(battle.getTerrainEffect()).thenReturn(terrainEffect);

    when(terrainEffect.getFireDamageMultiplier()).thenReturn(1.0);
    when(terrainEffect.getGrassHealingPercentage()).thenReturn(0.0);

    when(randomGenerator.nextInt(100)).thenReturn(0);
    when(randomGenerator.nextInt(16)).thenReturn(1);

    when(typeEffectiveness.calculateMultiplier(
        ElementalType.FIRE,
        ElementalType.GRASS
    )).thenReturn(2.0);

    when(damageCalculator.calculateDamage(
        attack,
        pokemon1,
        pokemon2,
        2.0,
        terrainEffect,
        false
    )).thenReturn(25);

    battleService.executeAttack(battle, attack);

    assertEquals(9, attack.getCurrentPP());

    verify(pokemon2).takeDamage(25);

    verify(damageCalculator).calculateDamage(
        attack,
        pokemon1,
        pokemon2,
        2.0,
        terrainEffect,
        false
    );

    verify(statusEffectManager).processEndOfTurn(pokemon1);
    verify(battle).registerTurn();
    verify(battle).advanceTurn();
    verify(resultRepository, never()).save(any(BattleResult.class));
    verify(battle, never()).finish(any(Trainer.class));
  }

  // Passes the critical flag when the generator returns zero.
  @Test
  void testAcertoCriticoComGeradorMockado() {
    Attack attack = new Attack(
        "Flamethrower",
        40,
        100,
        10,
        List.of()
    );

    when(battle.isStarted()).thenReturn(true);
    when(battle.isFinished()).thenReturn(false);

    when(battle.getCurrentTrainer()).thenReturn(trainer1);
    when(battle.getTrainer1()).thenReturn(trainer1);
    when(battle.getTrainer2()).thenReturn(trainer2);

    when(trainer1.getPokemon()).thenReturn(pokemon1);
    when(trainer2.getPokemon()).thenReturn(pokemon2);

    when(pokemon1.isAlive()).thenReturn(true);
    when(pokemon2.isAlive()).thenReturn(true);

    when(pokemon1.getName()).thenReturn("CharSal");
    when(pokemon1.getType()).thenReturn(ElementalType.FIRE);
    when(pokemon2.getType()).thenReturn(ElementalType.GRASS);

    when(pokemon1.getAttacks()).thenReturn(List.of(attack));

    when(battle.getTerrainEffect()).thenReturn(terrainEffect);

    when(terrainEffect.getFireDamageMultiplier()).thenReturn(1.0);
    when(terrainEffect.getGrassHealingPercentage()).thenReturn(0.0);

    when(randomGenerator.nextInt(100)).thenReturn(0);
    when(randomGenerator.nextInt(16)).thenReturn(0);

    when(typeEffectiveness.calculateMultiplier(
        ElementalType.FIRE,
        ElementalType.GRASS
    )).thenReturn(2.0);

    when(damageCalculator.calculateDamage(
        attack,
        pokemon1,
        pokemon2,
        2.0,
        terrainEffect,
        true
    )).thenReturn(38);

    battleService.executeAttack(battle, attack);

    assertEquals(9, attack.getCurrentPP());

    verify(pokemon2).takeDamage(38);

    verify(damageCalculator).calculateDamage(
        attack,
        pokemon1,
        pokemon2,
        2.0,
        terrainEffect,
        true
    );

    verify(statusEffectManager).processEndOfTurn(pokemon1);
    verify(battle).registerTurn();
    verify(battle).advanceTurn();
    verify(randomGenerator, atLeastOnce()).nextInt(eq(16));
  }

  // Rejects battles that have already started.
  @Test
  void shouldRejectAlreadyStartedBattle() {
    when(battle.isStarted()).thenReturn(true);

    assertThrows(
        IllegalStateException.class,
        () -> battleService.startBattle(battle, arena)
    );

    verifyNoInteractions(terrainService);
    verifyNoInteractions(turnManager);
  }

  // Rejects battles without both Pokemon.
  @Test
  void shouldRejectBattleWhenTrainerHasNoPokemon() {
    when(battle.isStarted()).thenReturn(false);
    when(battle.isFinished()).thenReturn(false);

    when(battle.getTrainer1()).thenReturn(trainer1);
    when(battle.getTrainer2()).thenReturn(trainer2);

    when(trainer1.getPokemon()).thenReturn(pokemon1);
    when(trainer2.getPokemon()).thenReturn(null);

    assertThrows(
        IllegalStateException.class,
        () -> battleService.startBattle(battle, arena)
    );

    verifyNoInteractions(terrainService);
    verifyNoInteractions(turnManager);
  }

  // Consumes PP and ends the turn when the attack misses.
  @Test
  void testGolpeErraQuandoPrecisaoFalha() {
    Attack attack = new Attack(
        "Flamethrower",
        40,
        1,
        10,
        List.of()
    );

    when(battle.isStarted()).thenReturn(true);
    when(battle.isFinished()).thenReturn(false);

    when(battle.getCurrentTrainer()).thenReturn(trainer1);
    when(battle.getTrainer1()).thenReturn(trainer1);
    when(battle.getTrainer2()).thenReturn(trainer2);

    when(trainer1.getPokemon()).thenReturn(pokemon1);


    when(pokemon1.isAlive()).thenReturn(true);
    when(pokemon1.getAttacks()).thenReturn(List.of(attack));

    when(battle.getTerrainEffect()).thenReturn(terrainEffect);
    when(terrainEffect.getGrassHealingPercentage()).thenReturn(0.0);

    when(randomGenerator.nextInt(100)).thenReturn(99);

    battleService.executeAttack(battle, attack);

    assertEquals(9, attack.getCurrentPP());

    verify(pokemon2, never()).takeDamage(anyInt());
    verify(randomGenerator, never()).nextInt(16);

    verify(damageCalculator, never()).calculateDamage(
        any(), any(), any(), anyDouble(), any(), anyBoolean()
    );

    verify(statusEffectManager).processEndOfTurn(pokemon1);
    verify(battle).registerTurn();
    verify(battle).advanceTurn();
  }


  // Rejects attacks when the battle has not started.
  @Test
  void shouldRejectAttackWhenBattleHasNotStarted() {
    Attack attack = new Attack(
        "Flamethrower",
        40,
        100,
        10,
        List.of()
    );

    when(battle.isStarted()).thenReturn(false);


    assertThrows(
        IllegalStateException.class,
        () -> battleService.executeAttack(battle, attack)
    );

    verify(battle, never()).getCurrentTrainer();
  }

  // Verifies the order of registered battle events.
  @Test
  void testSequenciaDeEventosNoTurno() {
    Attack attack = new Attack(
        "Flamethrower",
        40,
        100,
        10,
        List.of(new InflictStatusEffect(new BurnEffect()))
    );

    when(battle.isStarted()).thenReturn(true);
    when(battle.isFinished()).thenReturn(false);

    when(battle.getCurrentTrainer()).thenReturn(trainer1);
    when(battle.getTrainer1()).thenReturn(trainer1);
    when(battle.getTrainer2()).thenReturn(trainer2);

    when(trainer1.getPokemon()).thenReturn(pokemon1);
    when(trainer2.getPokemon()).thenReturn(pokemon2);

    when(pokemon1.isAlive()).thenReturn(true);
    when(pokemon2.isAlive()).thenReturn(true);

    when(pokemon1.getName()).thenReturn("CharSal");
    when(pokemon1.getType()).thenReturn(ElementalType.FIRE);
    when(pokemon2.getType()).thenReturn(ElementalType.GRASS);

    when(pokemon1.getAttacks()).thenReturn(List.of(attack));

    when(battle.getTerrainEffect()).thenReturn(terrainEffect);

    when(terrainEffect.getFireDamageMultiplier()).thenReturn(1.15);
    when(terrainEffect.getGrassHealingPercentage()).thenReturn(0.0);

    when(randomGenerator.nextInt(100)).thenReturn(0);
    when(randomGenerator.nextInt(16)).thenReturn(1);

    when(typeEffectiveness.calculateMultiplier(
        ElementalType.FIRE,
        ElementalType.GRASS
    )).thenReturn(2.0);

    when(damageCalculator.calculateDamage(
        attack,
        pokemon1,
        pokemon2,
        2.0,
        terrainEffect,
        false
    )).thenReturn(25);

    battleService.executeAttack(battle, attack);

    InOrder order = inOrder(battleRegister);
    order.verify(battleRegister).register(argThat(
        event -> event.getType() == BattleEventType.ATTACK));
    order.verify(battleRegister).register(argThat(
        event -> event.getType() == BattleEventType.DAMAGE));
    order.verify(battleRegister).register(argThat(
        event -> event.getType() == BattleEventType.STATUS_EFFECT));
    order.verify(battleRegister).register(argThat(
        event -> event.getType() == BattleEventType.TERRAIN_EFFECT));
    verifyNoMoreInteractions(battleRegister);

    verify(pokemon2).takeDamage(25);
  }


  // Saves the result after the battle finishes.
  @Test
  void testResultadoSalvoAoFimDaBatalha() {
    Attack attack = new Attack(
        "Flamethrower",
        40,
        100,
        10,
        List.of()
    );

    when(battle.isStarted()).thenReturn(true);
    when(battle.isFinished()).thenReturn(false);

    when(battle.getCurrentTrainer()).thenReturn(trainer1);
    when(battle.getTrainer1()).thenReturn(trainer1);
    when(battle.getTrainer2()).thenReturn(trainer2);

    when(trainer1.getPokemon()).thenReturn(pokemon1);
    when(trainer2.getPokemon()).thenReturn(pokemon2);

    when(pokemon1.isAlive()).thenReturn(true);
    when(pokemon2.isAlive()).thenReturn(true, false);

    when(pokemon1.getName()).thenReturn("CharSal");
    when(pokemon1.getType()).thenReturn(ElementalType.FIRE);
    when(pokemon2.getType()).thenReturn(ElementalType.GRASS);

    when(pokemon1.getAttacks()).thenReturn(List.of(attack));

    when(battle.getTerrainEffect()).thenReturn(terrainEffect);
    when(battle.getTurnCount()).thenReturn(5);

    when(terrainEffect.getFireDamageMultiplier()).thenReturn(1.0);

    when(randomGenerator.nextInt(100)).thenReturn(0);
    when(randomGenerator.nextInt(16)).thenReturn(1);

    when(typeEffectiveness.calculateMultiplier(
        ElementalType.FIRE,
        ElementalType.GRASS
    )).thenReturn(2.0);

    when(damageCalculator.calculateDamage(
        attack,
        pokemon1,
        pokemon2,
        2.0,
        terrainEffect,
        false
    )).thenReturn(100);

    doAnswer(invocation -> {
      when(battle.isFinished()).thenReturn(true);
      when(battle.getWinner()).thenReturn(trainer1);
      return null;
    }).when(battle).finish(trainer1);

    battleService.executeAttack(battle, attack);

    ArgumentCaptor<BattleResult> resultCaptor =
        ArgumentCaptor.forClass(BattleResult.class);

    verify(resultRepository).save(resultCaptor.capture());

    BattleResult result = resultCaptor.getValue();

    assertSame(trainer1, result.getWinner());
    assertSame(trainer2, result.getLoser());
    assertSame(pokemon1, result.getWinnerPokemon());
    assertSame(pokemon2, result.getLoserPokemon());
    assertEquals(5, result.getTurnCount());

    verify(battle).finish(trainer1);
  }

  // Keeps the battle finished when result persistence fails.
  @Test
  void testFalhaNoRepositorioNaoInvalidaBatalha() {
    Attack attack = new Attack(
        "Flamethrower",
        40,
        100,
        10,
        List.of()
    );

    when(battle.isStarted()).thenReturn(true);
    when(battle.isFinished()).thenReturn(false);

    when(battle.getCurrentTrainer()).thenReturn(trainer1);
    when(battle.getTrainer1()).thenReturn(trainer1);
    when(battle.getTrainer2()).thenReturn(trainer2);

    when(trainer1.getPokemon()).thenReturn(pokemon1);
    when(trainer2.getPokemon()).thenReturn(pokemon2);

    when(pokemon1.isAlive()).thenReturn(true);
    when(pokemon2.isAlive()).thenReturn(true, false);

    when(pokemon1.getName()).thenReturn("CharSal");
    when(pokemon1.getType()).thenReturn(ElementalType.FIRE);
    when(pokemon2.getType()).thenReturn(ElementalType.GRASS);

    when(pokemon1.getAttacks()).thenReturn(List.of(attack));

    when(battle.getTerrainEffect()).thenReturn(terrainEffect);

    when(terrainEffect.getFireDamageMultiplier()).thenReturn(1.0);

    when(randomGenerator.nextInt(100)).thenReturn(0);
    when(randomGenerator.nextInt(16)).thenReturn(1);

    when(typeEffectiveness.calculateMultiplier(
        ElementalType.FIRE,
        ElementalType.GRASS
    )).thenReturn(2.0);

    when(damageCalculator.calculateDamage(
        attack,
        pokemon1,
        pokemon2,
        2.0,
        terrainEffect,
        false
    )).thenReturn(100);

    doThrow(new RuntimeException("Repository unavailable"))
        .when(resultRepository)
        .save(any(BattleResult.class));

    doAnswer(invocation -> {
      when(battle.isFinished()).thenReturn(true);
      when(battle.getWinner()).thenReturn(trainer1);
      return null;
    }).when(battle).finish(trainer1);

    battleService.executeAttack(battle, attack);

    verify(battle).finish(trainer1);
    verify(resultRepository).save(any(BattleResult.class));
  }

}
