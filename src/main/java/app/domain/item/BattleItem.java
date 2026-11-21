package app.domain.item;


import app.domain.model.Pokemon;

/**
 * Defines the contract for items that can be used during a battle.
 */
public interface BattleItem {


  String getName();


  void use(Pokemon target);
}