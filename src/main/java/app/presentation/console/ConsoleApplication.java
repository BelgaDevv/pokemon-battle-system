package app.presentation.console;

import app.domain.application.battle.BattleService;
import app.domain.application.catalog.PokemonCatalog;
import app.domain.application.controller.BattleController;
import app.domain.application.port.ResultRepository;
import app.domain.application.port.TerrainService;
import app.domain.application.tournament.TournamentManager;
import app.domain.item.Antidote;
import app.domain.item.BattleItem;
import app.domain.item.Potion;
import app.domain.item.SuperPotion;
import app.domain.model.*;
import app.domain.terrain.*;
import app.domain.tournament.Tournament;
import app.infrastructure.logging.BattleLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Handles console menus for battles, tournaments,
 * Pokemon information and battle records.
 */
public class ConsoleApplication {

  private final Scanner scanner;

  private final BattleController battleController;
  private final BattleService battleService;
  private final PokemonCatalog catalog;
  private final TerrainService terrainService;
  private final BattleLogger battleLogger;
  private final ResultRepository resultRepository;

  private final List<BattleItem> items = List.of(new Potion(20), new SuperPotion(50), new Antidote());

  public ConsoleApplication(BattleController battleController, BattleService battleService, PokemonCatalog catalog, TerrainService terrainService, BattleLogger battleLogger, ResultRepository resultRepository) {
    this.battleController = Objects.requireNonNull(battleController);
    this.battleService = Objects.requireNonNull(battleService);
    this.catalog = Objects.requireNonNull(catalog);
    this.terrainService = Objects.requireNonNull(terrainService);
    this.battleLogger = Objects.requireNonNull(battleLogger);
    this.resultRepository = Objects.requireNonNull(resultRepository);
    this.scanner = new Scanner(System.in);
  }

  /**
   * Runs the main menu until the user chooses to exit.
   */
  public void run() {

    boolean running = true;

    while (running) {

      printTitle("POKESAL - POKEMON BATTLE SYSTEM");

      System.out.println("1 - Individual battle");
      System.out.println("2 - Tournament");
      System.out.println("3 - Pokemon catalog");
      System.out.println("4 - View results");
      System.out.println("5 - View events");
      System.out.println("6 - Demonstrate terrains");
      System.out.println("0 - Exit");

      try {
        switch (readOption()) {
          case 1 -> startIndividualBattle();
          case 2 -> startTournament();
          case 3 -> displayCatalog();
          case 4 -> displayResults();
          case 5 -> displayEvents();
          case 6 -> demonstrateTerrains();
          case 0 -> running = false;
          default -> System.out.println("Invalid option.");
        }
      } catch (IllegalArgumentException | IllegalStateException exception) {

        System.out.println("Error: " + exception.getMessage());
      }
    }

    scanner.close();

    System.out.println("Application closed.");
  }

  private void startIndividualBattle() {

    printTitle("INDIVIDUAL BATTLE");

    Trainer trainer1 = createTrainer(1);
    Trainer trainer2 = createTrainer(2);

    Arena arena = selectArena();

    Battle battle = new Battle(trainer1, trainer2);

    battleController.startBattle(battle, arena);

    playBattle(battle);
  }

  private Trainer createTrainer(int number) {

    System.out.print("\nTrainer name " + number + ": ");

    Trainer trainer = new Trainer(scanner.nextLine());

    List<Pokemon> available = catalog.getAvailablePokemon();

    System.out.println("\nChoose a Pokemon:");

    for (int i = 0; i < available.size(); i++) {

      Pokemon pokemon = available.get(i);

      System.out.printf("%d - %s [%s]%n", i + 1, pokemon.getName(), pokemon.getType());
    }

    int option = readOption();

    if (option < 1 || option > available.size()) {
      throw new IllegalArgumentException("Invalid Pokemon.");
    }

    String species = available.get(option - 1).getName();
    trainer.setPokemon(catalog.createPokemon(species));

    return trainer;
  }

  private Arena selectArena() {

    printTitle("SELECT AN ARENA");

    System.out.println("1 - Hot Asphalt");
    System.out.println("2 - Rain Puddle");
    System.out.println("3 - Central Garden");
    System.out.println("4 - Normal Terrain");

    return switch (readOption()) {
      case 1 -> new Arena("Hot Asphalt", new HotAsphalt());
      case 2 -> new Arena("Rain Puddle", new RainPuddle());
      case 3 -> new Arena("Central Garden", new CentralGarden());
      case 4 -> new Arena("Normal Terrain", new NormalTerrain());
      default -> throw new IllegalArgumentException("Invalid arena.");
    };
  }

  /**
   * Displays the time of day and weather assigned to the battle.
   */
  private void displayBattleEnvironment(Battle battle) {
    TerrainContext context = battle.getTerrainContext();

    System.out.println("\nBattle conditions:");
    System.out.println("Time of day: " + context.getPeriodOfDay());
    System.out.println("Weather: " + context.getWeatherCondition());
  }

  private void playBattle(Battle battle) {

    printTitle("BATTLE STARTED");

    System.out.println("First trainer: " + battle.getCurrentTrainer().getName());

    displayBattleEnvironment(battle);
    displayTerrainEffect(battle);

    while (!battle.isFinished()) {

      displayBattleState(battle);

      Trainer current = battle.getCurrentTrainer();

      System.out.println("\nTurn: " + current.getName());

      System.out.println("1 - Attack");
      System.out.println("2 - Use item");
      System.out.println("3 - View stats");

      int previousEvents = battleLogger.getEvents().size();

      try {
        switch (readOption()) {
          case 1 -> selectAttack(battle);
          case 2 -> selectItem(battle);
          case 3 -> displayPokemon(current.getPokemon());
          default -> System.out.println("Invalid option.");
        }
      } catch (IllegalArgumentException | IllegalStateException exception) {

        System.out.println("Action rejected: " + exception.getMessage());
      }

      displayNewEvents(previousEvents);
    }

    printTitle("BATTLE FINISHED");

    System.out.println("Winner: " + battle.getWinner().getName());

    System.out.println("Pokemon: " + battle.getWinner().getPokemon().getName());

    System.out.println("Total turns: " + battle.getTurnCount());
  }

  private void selectAttack(Battle battle) {

    Pokemon pokemon = battle.getCurrentTrainer().getPokemon();

    List<Attack> attacks = pokemon.getAttacks();

    System.out.println("\nAvailable attacks:");

    for (int i = 0; i < attacks.size(); i++) {

      Attack attack = attacks.get(i);

      System.out.printf("%d - %s | Power: %d | Accuracy: %.0f%% | PP: %d/%d%n", i + 1, attack.getName(), attack.getPower(), attack.getAccuracy(), attack.getCurrentPP(), attack.getMaxPP());
    }

    int option = readOption();

    if (option < 1 || option > attacks.size()) {
      throw new IllegalArgumentException("Invalid attack.");
    }

    battleController.executeAttack(battle, attacks.get(option - 1));
  }

  private void selectItem(Battle battle) {

    Trainer trainer = battle.getCurrentTrainer();

    System.out.println("\nItems used: " + trainer.getItemsUsed());

    for (int i = 0; i < items.size(); i++) {

      System.out.printf("%d - %s%n", i + 1, items.get(i).getName());
    }

    int option = readOption();

    if (option < 1 || option > items.size()) {
      throw new IllegalArgumentException("Invalid item.");
    }

    battleController.useItem(battle, items.get(option - 1));
  }

  private void displayBattleState(Battle battle) {

    printTitle("TURN " + (battle.getTurnCount() + 1));

    displayTrainer(battle.getTrainer1());

    System.out.println("\n          VS\n");

    displayTrainer(battle.getTrainer2());
  }

  private void displayTrainer(Trainer trainer) {

    Pokemon pokemon = trainer.getPokemon();

    System.out.printf("%s - %s [%s]%n", trainer.getName(), pokemon.getName(), pokemon.getType());

    System.out.printf("HP: %d/%d%n", pokemon.getCurrentHp(), pokemon.getMaxHp());

    System.out.println("Status: " + pokemon.getStatusEffects().stream().map(effect -> effect.getType().name()).toList());

    System.out.println("Items used: " + trainer.getItemsUsed());
  }

  private void displayPokemon(Pokemon pokemon) {

    System.out.println("\nPokemon: " + pokemon.getName());
    System.out.println("Type: " + pokemon.getType());

    System.out.printf("HP: %d/%d%n", pokemon.getCurrentHp(), pokemon.getMaxHp());

    System.out.printf("ATK: %d -> %.2f%n", pokemon.getBaseAttack(), pokemon.getEffectiveAttack());

    System.out.printf("DEF: %d -> %.2f%n", pokemon.getBaseDefense(), pokemon.getEffectiveDefense());

    System.out.printf("SPD: %d -> %.2f%n", pokemon.getBaseSpeed(), pokemon.getEffectiveSpeed());

    System.out.println("Status effects: " + pokemon.getStatusEffects().stream().map(effect -> effect.getType().name()).toList());

    pokemon.getStatModifiers().forEach(modifier -> System.out.println("Modifier: " + modifier.getStatType() + " x" + modifier.getMultiplier()));
  }

  private void displayCatalog() {

    printTitle("POKEMON CATALOG");

    for (Pokemon pokemon : catalog.getAvailablePokemon()) {

      displayPokemon(pokemon);

      for (Attack attack : pokemon.getAttacks()) {

        System.out.printf("  %s | Power: %d | PP: %d | Accuracy: %.0f%%%n", attack.getName(), attack.getPower(), attack.getMaxPP(), attack.getAccuracy());
      }

      System.out.println("------------------------------");
    }
  }

  private void startTournament() {

    printTitle("POKESAL TOURNAMENT");

    System.out.println("1 - 4 participants");
    System.out.println("2 - 8 participants");

    int size = switch (readOption()) {
      case 1 -> 4;
      case 2 -> 8;
      default -> throw new IllegalArgumentException("Invalid participant count.");
    };

    List<Trainer> participants = new ArrayList<>();

    for (int i = 0; i < size; i++) {
      participants.add(createTrainer(i + 1));
    }

    Tournament tournament = new Tournament(participants);

    TournamentManager manager = new TournamentManager(tournament, battleService);

    Arena arena = selectArena();

    while (!manager.isFinished()) {

      printTitle("ROUND " + tournament.getBracket().getCurrentRoundNumber());

      List<Trainer> matchup = tournament.getBracket().getCurrentMatch();

      // Creates independent Pokemon for each new match.
      for (Trainer trainer : matchup) {

        String species = trainer.getPokemon().getName();

        trainer.setPokemon(catalog.createPokemon(species));
      }

      Battle battle = manager.startNextMatch(arena);

      playBattle(battle);

      BattleResult result = manager.completeCurrentMatch();

      System.out.println("Qualified: " + result.getWinner().getName());
    }

    printTitle("TOURNAMENT FINISHED");

    System.out.println("CHAMPION: " + tournament.getBracket().getChampion().getName());
  }

  private void displayResults() {

    printTitle("SAVED RESULTS");

    List<BattleResult> results = resultRepository.findAll();

    if (results.isEmpty()) {
      System.out.println("No completed battles.");
      return;
    }

    for (BattleResult result : results) {

      System.out.println("Winner: " + result.getWinner().getName());

      System.out.println("Winning Pokemon: " + result.getWinnerPokemon().getName());

      System.out.println("Loser: " + result.getLoser().getName());

      System.out.println("Defeated Pokemon: " + result.getLoserPokemon().getName());

      System.out.println("Turns: " + result.getTurnCount());

      System.out.println("------------------------------");
    }
  }

  private void displayEvents() {

    printTitle("BATTLE EVENTS");

    if (battleLogger.getEvents().isEmpty()) {
      System.out.println("No events recorded.");
      return;
    }

    displayNewEvents(0);
  }

  private void displayNewEvents(int startIndex) {

    var events = battleLogger.getEvents();

    for (int i = startIndex; i < events.size(); i++) {

      var event = events.get(i);

      System.out.printf("[%s] %s%n", event.getType(), event.getDescription());
    }
  }

  private void displayTerrainEffect(Battle battle) {

    var effect = battle.getTerrainEffect();

    System.out.println("\nArena effects:");

    System.out.printf("FIRE multiplier: %.2f%n", effect.getFireDamageMultiplier());

    System.out.printf("WATER multiplier: %.2f%n", effect.getWaterDamageMultiplier());

    System.out.printf("WATER accuracy bonus: %.0f%%%n", effect.getWaterAccuracyBonus() * 100);

    System.out.printf("GRASS healing: %.0f%%%n", effect.getGrassHealingPercentage() * 100);
  }

  private void demonstrateTerrains() {

    printTitle("TERRAIN DEMONSTRATION");

    var context = terrainService.createBattleContext();

    System.out.println("Time of day: " + context.getPeriodOfDay());

    System.out.println("Weather: " + context.getWeatherCondition());

    List<Arena> arenas = List.of(new Arena("Hot Asphalt", new HotAsphalt()), new Arena("Rain Puddle", new RainPuddle()), new Arena("Central Garden", new CentralGarden()), new Arena("Normal Terrain", new NormalTerrain()));

    for (Arena arena : arenas) {

      var effect = terrainService.calculateEffect(arena, context);

      System.out.println("\nArena: " + arena.getName());

      System.out.printf("FIRE damage: x%.2f%n", effect.getFireDamageMultiplier());

      System.out.printf("WATER damage: x%.2f%n", effect.getWaterDamageMultiplier());

      System.out.printf("WATER accuracy: +%.0f%%%n", effect.getWaterAccuracyBonus() * 100);

      System.out.printf("GRASS healing: %.0f%%%n", effect.getGrassHealingPercentage() * 100);
    }
  }

  private int readOption() {

    System.out.print("\nOption: ");

    try {
      return Integer.parseInt(scanner.nextLine().trim());
    } catch (NumberFormatException exception) {
      return -1;
    }
  }

  private void printTitle(String title) {

    System.out.println("\n==========================================");

    System.out.println(title);

    System.out.println("==========================================");
  }
}
