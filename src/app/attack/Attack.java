package app.attack;

import app.type.Type;

public class Attack {

    private String name;
    private int ppMax;
    private int ppCurrent;
    private double damage;
    private Type type;
    private StatusEffect statusEffect;
    private AttackEffect attackEffect;

    //class constructor
    public Attack(AttackEffect attackEffect, double damage, String name, int ppCurrent,
                  int ppMax, StatusEffect statusEffect, Type type) {

        this.attackEffect = attackEffect;
        this.damage = damage;
        this.name = name;
        this.ppCurrent = ppCurrent;
        this.ppMax = ppMax;
        this.statusEffect = statusEffect;
        this.type = type;
    }

    public AttackEffect getAttackEffect() {
        return attackEffect;
    }

    public void setAttackEffect(AttackEffect attackEffect) {
        this.attackEffect = attackEffect;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPpCurrent() {
        return ppCurrent;
    }

    public void setPpCurrent(int ppCurrent) {
        this.ppCurrent = ppCurrent;
    }

    public int getPpMax() {
        return ppMax;
    }

    public void setPpMax(int ppMax) {
        this.ppMax = ppMax;
    }

    public StatusEffect getStatusEffect() {
        return statusEffect;
    }

    public void setStatusEffect(StatusEffect statusEffect) {
        this.statusEffect = statusEffect;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
