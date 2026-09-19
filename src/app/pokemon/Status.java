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

    public void setName(String name) {
        this.name = name;
    }

    public StatusEffect getStatusEffect() {
        return statusEffect;
    }

    public void setStatusEffect(StatusEffect statusEffect) {
        this.statusEffect = statusEffect;
    }
}
