package app;

import app.arena.Arena;
import app.arena.ArenaEffect;
import app.attack.Attack;
import app.attack.AttackEffect;
import app.attack.StatusEffect;
import app.battle.Battle;
import app.Interaction;
import app.item.Item;
import app.item.ItemEffect;
import app.pokemon.Pokemon;
import app.pokemon.Status;
import app.trainer.Trainer;
import app.type.Type;

public class Main {

    public static void main(String[] args) {

        // =========================
        // Effects
        // =========================

        StatusEffect statusEffect = new StatusEffect();
        AttackEffect attackEffect = null;
        ItemEffect itemEffect = new ItemEffect();

        // =========================
        // Status
        // =========================

        Status burn = new Status("Burn", statusEffect);
        Status poison = new Status("Poison", statusEffect);
        Status paralysis = new Status("Paralysis", statusEffect);

        // =========================
        // Trainer 1 - Fire
        // =========================

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

        // =========================
        // Trainer 2 - Plant
        // =========================

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

        // =========================
        // Items
        // =========================

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

        // =========================
        // Trainers
        // =========================

        Trainer trainer1 = new Trainer(
                "Trainer 1",
                firePokemon,
                trainer1Items
        );

        Trainer trainer2 = new Trainer(
                "Trainer 2",
                plantPokemon,
                trainer2Items
        );

        // =========================
        // Arena
        // =========================

        ArenaEffect arenaEffect = new ArenaEffect(
                ArenaEffect.EffectType.HOT_ASPHALT
        );

        Arena arena = new Arena(
                arenaEffect,
                "Hot Asphalt"
        );

        // =========================
        // Battle
        // =========================

        Battle battle = new Battle(
                arena,
                trainer1,
                trainer2
        );

        // =========================
        // Interaction
        // =========================

        Interaction interaction = new Interaction();

        interaction.startBattle(battle);
    }
}