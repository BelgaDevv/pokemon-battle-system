package app.domain.item;


import app.domain.model.Pokemon;

/**
 * advanced healing item that restores a configured
 * amount of HP.
 */
public class SuperPotion implements BattleItem {

  private final int healingAmount;

  public SuperPotion(int healingAmount) {
    if (healingAmount <= 0) {
      throw new IllegalArgumentException(
          "Healing amount must be greater than zero."
      );
    }

    this.healingAmount = healingAmount;
  }

  @Override
  public String getName() {
    return "Super Potion";
  }

  @Override
  public void use(Pokemon target) {
    if (target == null) {
      throw new IllegalArgumentException(
          "Target Pokemon cannot be null."
      );
    }

    target.heal(healingAmount);
  }
}