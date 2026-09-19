package app.arena;

public class Arena {

    private String name;
    private ArenaEffect arenaEffect;

    public Arena(ArenaEffect arenaEffect, String name) {
        this.arenaEffect = arenaEffect;
        this.name = name;
    }

    public ArenaEffect getArenaEffect() {
        return arenaEffect;
    }

    public String getName() {
        return name;
    }

}
