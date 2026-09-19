package app.attack;

import app.pokemon.Pokemon;

public class StatusEffect {

    public void applyBurn(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null.");
        }

        pokemon.setAtk(pokemon.getAtk() * 0.50);
    }

    public void processBurn(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null.");
        }

        double damage = pokemon.getHpMax() * 0.06;
        pokemon.setHpCurrent(pokemon.getHpCurrent() - damage);
    }

    public void processPoison(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null.");
        }

        double damage = pokemon.getHpMax() * 0.04;
        pokemon.setHpCurrent(pokemon.getHpCurrent() - damage);
    }

    public void applyParalysis(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null.");
        }

        pokemon.setSpe(pokemon.getSpe() * 0.50);
    }
}