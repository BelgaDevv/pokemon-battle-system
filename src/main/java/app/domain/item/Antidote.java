package app.domain.item;


import app.domain.enums.StatusType;
import app.domain.model.Pokemon;

/**
 * an item that removes the poison status condition
 * from a Pokemon.
 */
public class Antidote implements BattleItem {

  @Override
  public String getName() {
    return "Antidote";
  }

  @Override
  public void use(Pokemon target) {
    if (target == null) {
      throw new IllegalArgumentException(
          "Target Pokemon cannot be null."
      );
    }

    target.removeStatusEffect(StatusType.POISON);
  }
}