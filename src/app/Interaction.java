package app;

import app.attack.Attack;
import app.battle.Battle;
import app.pokemon.Pokemon;

import java.util.Scanner;

/**
 * Handles user interaction during a battle.
 */
public class Interaction {

  private final Scanner scanner;

  /**
   * Creates a new interaction handler.
   */
  public Interaction() {
    this.scanner = new Scanner(System.in);
  }

  /**
   * Permite ao treinador escolher um Pokésal a partir de uma lista de opções disponíveis.
   */
  public Pokemon chooseInitial(String trainerName, Pokemon[] disponiveis, String[] nomes) {
    System.out.println("\n=================================");
    System.out.println(trainerName + ", escolha seu Pokésal inicial:");
    System.out.println("=================================");

    for (int i = 0; i < disponiveis.length; i++) {
      System.out.println((i + 1) + " - " + nomes[i] + " (Tipo: " + disponiveis[i].getType() + ")");
    }
    System.out.println("=================================");

    while (true) {
      System.out.print("Escolha o número do seu Pokésal: ");
      if (!scanner.hasNextInt()) {
        scanner.next();
        System.out.println("Opção inválida. Digite um número válido.");
        continue;
      }

      int opcao = scanner.nextInt();
      if (opcao >= 1 && opcao <= disponiveis.length) {
        // Retorna uma cópia ou o próprio pokémon escolhido
        return disponiveis[opcao - 1];
      }

      System.out.println("Opção inválida.");
    }
  }

  /**
   * Starts the battle interaction loop.
   */
  public void startBattle(Battle battle) {
    while (battle.getWinner() == null) {
      runTurn(battle);
    }

    System.out.println();
    System.out.println("Battle finished!");
    System.out.println(
        "Winner: " + battle.getWinner().getName()
    );
  }

  /**
   * Runs the current trainer's turn.
   */
  private void runTurn(Battle battle) {
    Pokemon pokemon = battle.getCurrentTrainer().getPokemon();

    System.out.println();
    System.out.println("================================");
    System.out.println("Round: " + battle.getRound());
    System.out.println(
        "Turn: " + battle.getCurrentTrainer().getName()
    );
    System.out.println(
        "Pokemon HP: "
            + pokemon.getHpCurrent()
            + "/"
            + pokemon.getHpMax()
    );
    System.out.println("================================");

    Attack selectedAttack = chooseAttack(pokemon);

    battle.executeAttack(selectedAttack);
  }

  /**
   * Displays the available attacks and allows the user to select one.
   */
  private Attack chooseAttack(Pokemon pokemon) {
    Attack[] attacks = pokemon.getAttack();

    System.out.println("Choose an attack:");

    for (int i = 0; i < attacks.length; i++) {
      Attack attack = attacks[i];

      System.out.println(
          (i + 1)
              + " - "
              + attack.getName()
              + " | PP: "
              + attack.getPpCurrent()
              + "/"
              + attack.getPpMax()
      );
    }

    while (true) {
      System.out.print("Option: ");

      if (!scanner.hasNextInt()) {
        scanner.next();
        System.out.println("Invalid option.");
        continue;
      }

      int option = scanner.nextInt();

      if (option >= 1 && option <= attacks.length) {
        return attacks[option - 1];
      }

      System.out.println("Invalid option.");
    }
  }
}