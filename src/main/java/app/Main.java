
package app;

import app.domain.application.battle.BattleService;
import app.domain.application.catalog.PokemonCatalog;
import app.domain.application.controller.BattleController;
import app.domain.service.*;
import app.infrastructure.logging.BattleLogger;
import app.infrastructure.persistence.InMemoryResultRepository;
import app.infrastructure.random.RandomGeneratorImpl;
import app.infrastructure.terrain.TerrainServiceImpl;
import app.infrastructure.weather.RandomWeatherProvider;
import app.presentation.console.ConsoleApplication;

import java.time.Clock;

/**
 * Configures the application dependencies and starts
 * the Pokemon battle system.
 */
public class Main {

  public static void main(String[] args) {

    RandomGeneratorImpl random = new RandomGeneratorImpl();

    BattleLogger battleLogger = new BattleLogger();
    InMemoryResultRepository resultRepository =
        new InMemoryResultRepository();

    PokemonCatalog pokemonCatalog = new PokemonCatalog();

    RandomWeatherProvider weatherProvider =
        new RandomWeatherProvider(random);

    TerrainServiceImpl terrainService =
        new TerrainServiceImpl(
            Clock.systemDefaultZone(),
            weatherProvider
        );

    DamageCalculator damageCalculator = new DamageCalculator();
    TurnManager turnManager = new TurnManager();
    ItemManager itemManager = new ItemManager();
    StatusEffectManager statusEffectManager = new StatusEffectManager();
    StatModifierManager statModifierManager = new StatModifierManager();
    TypeEffectiveness typeEffectiveness = new TypeEffectiveness();

    BattleService battleService = new BattleService(
        damageCalculator,
        turnManager,
        itemManager,
        statusEffectManager,
        statModifierManager,
        typeEffectiveness,
        terrainService,
        random,
        battleLogger,
        resultRepository
    );

    BattleController battleController =
        new BattleController(battleService);

    ConsoleApplication application = new ConsoleApplication(
        battleController,
        battleService,
        pokemonCatalog,
        terrainService,
        battleLogger,
        resultRepository
    );

    application.run();
  }
}
