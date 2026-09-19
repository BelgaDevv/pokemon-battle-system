package app.attack;

import app.pokemon.Pokemon;

public class AttackEffect {

    public void increaseSpeed(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null.");
        }

        pokemon.setSpe(pokemon.getSpe() * 1.10);
    }

    public void increaseAttack(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null.");
        }

        pokemon.setAtk(pokemon.getAtk() * 1.10);
    }

    public void increaseDefense(Pokemon pokemon) {
        if (pokemon == null) {
            throw new IllegalArgumentException("Pokemon cannot be null.");
        }

        pokemon.setDef(pokemon.getDef() * 1.10);
    }
}