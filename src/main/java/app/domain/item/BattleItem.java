package app.domain.item;


import app.domain.model.Pokemon;

/**
 * Defines the contract for items that can be used during a battle.
 */
public interface BattleItem {

  // return the item`s display name
  String getName();

  //Applies the item's effect to the target Pokemon.
  void use(Pokemon target);
}