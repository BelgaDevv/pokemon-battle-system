package app.item;

import app.pokemon.Pokemon;

public class ItemEffect {

    public void heal(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null.");
        }

        double newHp = pokemon.getHpCurrent() + 20;

        if (newHp > pokemon.getHpMax()) {
            newHp = pokemon.getHpMax();
        }

        pokemon.setHpCurrent(newHp);
    }

    public void clearBurn(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null.");
        }

        if (pokemon.getStatus() != null
                && "Burn".equals(pokemon.getStatus().getName())) {
            pokemon.setStatus(null);
        }
    }

    public void clearPoison(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null.");
        }

        if (pokemon.getStatus() != null
                && "Poison".equals(pokemon.getStatus().getName())) {
            pokemon.setStatus(null);
        }
    }

    public void clearParalysis(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null.");
        }

        if (pokemon.getStatus() != null
                && "Paralysis".equals(pokemon.getStatus().getName())) {
            pokemon.setStatus(null);
        }
    }
}