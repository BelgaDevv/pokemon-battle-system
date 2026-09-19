package app.arena;

import app.attack.Attack;
import app.pokemon.Pokemon;
import app.type.Type;

public class ArenaEffect {

    public enum EffectType {
        HOT_ASPHALT,
        RAIN_PUDDLE,
        CENTRAL_FLOWERBED
    }

    private EffectType effectType;

    public ArenaEffect(EffectType effectType) {
        this.effectType = effectType;
    }

    public double fireDamageBonus(double damage, Attack attack) {
        if (effectType == EffectType.HOT_ASPHALT
                && attack.getType() == Type.FIRE) {
            return damage * 1.15;
        }

        return damage;
    }

    public double waterDamageBonus(double damage, Attack attack) {
        if (effectType == EffectType.RAIN_PUDDLE
                && attack.getType() == Type.WATER) {
            return damage * 1.10;
        }

        return damage;
    }

    public void plantRecoverBonus(Pokemon pokemon) {
        if (effectType == EffectType.CENTRAL_FLOWERBED
                && pokemon.getType() == Type.PLANT) {

            double recovery = pokemon.getHpMax() * 0.05;
            double newHp = pokemon.getHpCurrent() + recovery;

            pokemon.setHpCurrent(
                    Math.min(newHp, pokemon.getHpMax())
            );
        }
    }

    public EffectType getEffectType() {
        return effectType;
    }
}