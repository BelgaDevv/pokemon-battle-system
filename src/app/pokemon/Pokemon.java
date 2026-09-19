package app.pokemon;

import app.attack.Attack;
import app.type.Type;

public class Pokemon {

    private double hpMax;
    private double hpCurrent;
    private double spe;
    private double def;
    private double atk;
    private Status status;

    private Type type;
    private Attack[] attack;

    public Pokemon(double atk, double def, double hpCurrent, double hpMax,
                   double spe, Type type, Attack[] attack) {

        if (hpMax <= 0) {
            throw new IllegalArgumentException("HP max must be greater than zero.");
        }

        if (hpCurrent < 0 || hpCurrent > hpMax) {
            throw new IllegalArgumentException(
                    "Current HP must be between zero and max HP.");
        }

        if (atk < 0 || def < 0 || spe < 0) {
            throw new IllegalArgumentException(
                    "ATK, DEF and SPE cannot be negative.");
        }

        validateAttacks(type, attack);

        this.atk = atk;
        this.def = def;
        this.hpCurrent = hpCurrent;
        this.hpMax = hpMax;
        this.spe = spe;
        this.type = type;
        this.status = null;
        this.attack = attack;
    }

    private void validateAttacks(Type pokemonType, Attack[] attacks) {

        if (attacks == null || attacks.length != 4) {
            throw new IllegalArgumentException(
                    "A Pokemon must have exactly 4 attacks.");
        }

        for (Attack attack : attacks) {

            if (attack == null) {
                throw new IllegalArgumentException(
                        "Pokemon cannot have a null attack.");
            }

            if (pokemonType.isWeakAgainst(attack.getType())) {
                throw new IllegalArgumentException(
                        "Pokemon cannot have an attack of its weakness type.");
            }
        }
    }

    public Attack[] getAttack() {
        return attack;
    }

    public void setAttack(Attack[] attack) {
        validateAttacks(this.type, attack);
        this.attack = attack;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getAtk() {
        return atk;
    }

    public void setAtk(double atk) {
        this.atk = atk;
    }

    public double getDef() {
        return def;
    }

    public void setDef(double def) {
        this.def = def;
    }

    public double getHpCurrent() {
        return hpCurrent;
    }

    public void setHpCurrent(double hpCurrent) {
        if (hpCurrent < 0) {
            this.hpCurrent = 0;
            return;
        }

        this.hpCurrent = Math.min(hpCurrent, hpMax);
    }

    public double getHpMax() {
        return hpMax;
    }

    public void setHpMax(double hpMax) {
        if (hpMax <= 0 || hpMax < hpCurrent) {
            throw new IllegalArgumentException(
                    "HP max must be greater than zero and current HP.");
        }

        this.hpMax = hpMax;
    }

    public double getSpe() {
        return spe;
    }

    public void setSpe(double spe) {
        this.spe = spe;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        validateAttacks(type, this.attack);
        this.type = type;
    }
}