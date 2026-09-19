package app.pokemon;

import app.attack.StatusEffect;

public class Status {

    private String name;
    private StatusEffect statusEffect;

    public Status(String name, StatusEffect statusEffect) {
        this.name = name;
        this.statusEffect = statusEffect;
    }

    public String getName() {
        return name;
    }

    public StatusEffect getStatusEffect() {
        return statusEffect;
    }
}