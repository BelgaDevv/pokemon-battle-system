
package app.pokemon;

/*
store Pokemons attributes and operation parameters
 */

import app.type.Type;
import app.attack.Attack;

public class Pokemon {

    private double hpMax;
    private double hpCurrent;
    private double spe;
    private double def;
    private double atk;

    private Type type;
    private Attack attack;

    // Constructor
    public Pokemon(double atk, double def, double hpCurrent, double hpMax, double spe, Type type) {
        this.atk = atk;
        this.def = def;
        this.hpCurrent = hpCurrent;
        this.hpMax = hpMax;
        this.spe = spe;
        this.type = type;
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
        this.hpCurrent = hpCurrent;
    }

    public double getHpMax() {
        return hpMax;
    }

    public void setHpMax(double hpMax) {
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
        this.type = type;
    }
}
