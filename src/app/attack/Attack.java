package app.attack;

import app.pokemon.Status;
import app.type.Type;

public class Attack {

    private String name;
    private int ppMax;
    private int ppCurrent;
    private double damage;
    private Type type;
    private Status status;
    private AttackEffect attackEffect;

    public Attack(AttackEffect attackEffect, double damage, String name, int ppCurrent,
                  int ppMax, Status status, Type type) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Attack name cannot be empty.");
        }

        if (ppMax <= 0) {
            throw new IllegalArgumentException("PP max must be greater than zero.");
        }

        if (ppCurrent < 0 || ppCurrent > ppMax) {
            throw new IllegalArgumentException(
                    "Current PP must be between zero and max PP.");
        }

        if (damage < 0) {
            throw new IllegalArgumentException(
                    "Damage cannot be negative.");
        }

        if (type == null) {
            throw new IllegalArgumentException(
                    "Attack type cannot be null.");
        }

        this.attackEffect = attackEffect;
        this.damage = damage;
        this.name = name;
        this.ppCurrent = ppCurrent;
        this.ppMax = ppMax;
        this.status = status;
        this.type = type;
    }

    public boolean canUse() {
        return ppCurrent > 0;
    }

    public boolean use() {
        if (!canUse()) {
            return false;
        }

        ppCurrent--;
        return true;
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

    public String getName() {
        return name;
    }

    public int getPpCurrent() {
        return ppCurrent;
    }

    public int getPpMax() {
        return ppMax;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }
}