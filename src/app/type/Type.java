package app.type;

/*
Name Pokemons elemental types
 */

public enum Type {

    PLANT,
    FIRE,
    WATER;

    public Type getAdvantage() {
        return switch (this) {
            case PLANT -> WATER;
            case FIRE -> PLANT;
            case WATER -> FIRE;
        };
    }

    public Type getDisadvantage() {
        return switch (this) {
            case PLANT -> FIRE;
            case FIRE -> WATER;
            case WATER -> PLANT;
        };
    }

    public double getDamageMultiplier(Type defenderType) {
        if (defenderType == getAdvantage()) {
            return 2.0;
        }

        if (defenderType == getDisadvantage()) {
            return 0.5;
        }

        return 1.0;
    }

    public boolean isWeakAgainst(Type attackType) {
        return attackType == getDisadvantage();
    }
}