package app;

import app.arena.Arena;
import app.arena.ArenaEffect;
import app.attack.Attack;
import app.attack.AttackEffect;
import app.attack.StatusEffect;
import app.battle.Battle;
import app.item.Item;
import app.item.ItemEffect;
import app.pokemon.Pokemon;
import app.pokemon.Status;
import app.trainer.Trainer;
import app.type.Type;

/**
 * Provides the entry point for the Pokemon battle application.
 */
public class Main {

  /**
   * Starts the application and initializes a battle.
   */
  public static void main(String[] args) {
    java.util.Random sorteador = new java.util.Random();
    Interaction interaction = new Interaction();

    // Effects


    StatusEffect statusEffect = new StatusEffect();
    AttackEffect attackEffect = null;
    ItemEffect itemEffect = new ItemEffect();


    // Status


    Status burn = new Status("Burn", statusEffect);
    Status poison = new Status("Poison", statusEffect);
    Status paralysis = new Status("Paralysis", statusEffect);


    // Trainer 1 - Fire


    Attack fireAttack1 = new Attack(
        attackEffect,
        20,
        "Fire Strike",
        10,
        10,
        burn,
        Type.FIRE
    );

    Attack fireAttack2 = new Attack(
        attackEffect,
        18,
        "Flame Hit",
        15,
        15,
        null,
        Type.FIRE
    );

    Attack plantAttack1 = new Attack(
        attackEffect,
        15,
        "Leaf Attack",
        10,
        10,
        poison,
        Type.PLANT
    );

    Attack plantAttack2 = new Attack(
        attackEffect,
        12,
        "Vine Hit",
        20,
        20,
        null,
        Type.PLANT
    );

    Attack[] firePokemonAttacks = {
        fireAttack1,
        fireAttack2,
        plantAttack1,
        plantAttack2
    };

    Pokemon firePokemon = new Pokemon(
        50,
        40,
        100,
        100,
        60,
        Type.FIRE,
        firePokemonAttacks
    );


    // Trainer 2 - Plant


    Attack plantAttack3 = new Attack(
        attackEffect,
        20,
        "Leaf Blade",
        10,
        10,
        poison,
        Type.PLANT
    );

    Attack plantAttack4 = new Attack(
        attackEffect,
        18,
        "Nature Hit",
        15,
        15,
        null,
        Type.PLANT
    );

    Attack waterAttack1 = new Attack(
        attackEffect,
        20,
        "Water Strike",
        10,
        10,
        paralysis,
        Type.WATER
    );

    Attack waterAttack2 = new Attack(
        attackEffect,
        15,
        "Water Pulse",
        15,
        15,
        null,
        Type.WATER
    );

    Attack[] plantPokemonAttacks = {
        plantAttack3,
        plantAttack4,
        waterAttack1,
        waterAttack2
    };

    Pokemon plantPokemon = new Pokemon(
        45,
        45,
        100,
        100,
        55,
        Type.PLANT,
        plantPokemonAttacks
    );

    // pokemons

    Pokemon[] pokesaisDisponiveis = { firePokemon, plantPokemon };
    String[] nomesPokesais = { "CharSal", "BulbaSal" };


    // Items


    Item potion1 = new Item(itemEffect, "Potion");
    Item antidote1 = new Item(itemEffect, "Antidote");

    Item potion2 = new Item(itemEffect, "Potion");
    Item burnHeal = new Item(itemEffect, "Burn Heal");

    Item[] trainer1Items = {
        potion1,
        antidote1
    };

    Item[] trainer2Items = {
        potion2,
        burnHeal
    };


    // Trainers


    Pokemon p1 = interaction.chooseInitial("Trainer 1", pokesaisDisponiveis, nomesPokesais);
    Pokemon p2 = interaction.chooseInitial("Trainer 2", pokesaisDisponiveis, nomesPokesais);

    Trainer trainer1 = new Trainer("Trainer 1", p1, trainer1Items);
    Trainer trainer2 = new Trainer("Trainer 2", p2, trainer2Items);


    // Arena

    int numeroArena = sorteador.nextInt(3) + 1;
    Arena arena;
    if (numeroArena == 1) {
      arena = new Arena(new ArenaEffect(ArenaEffect.EffectType.HOT_ASPHALT), "HOT_ASPHALT");
    } else if (numeroArena == 2) {
      arena = new Arena(new ArenaEffect(ArenaEffect.EffectType.RAIN_PUDDLE), "RAIN_PUDDLE");
    } else {
      arena = new Arena(new ArenaEffect(ArenaEffect.EffectType.CENTRAL_FLOWERBED), "CENTRAL_FLOWERBED");
    }
    System.out.println();
    System.out.println("========================================");
    System.out.println(" ARENA: " + arena.getName());
    System.out.println("========================================");


    Battle battle = new Battle(
        arena,
        trainer1,
        trainer2
    );


    // Interaction

    interaction.startBattle(battle);
  }
}