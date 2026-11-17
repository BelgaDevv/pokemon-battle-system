package app.domain.service;

import app.domain.item.BattleItem;
import app.domain.model.Trainer;

import java.util.Objects;

/**
 * Controls item usage during Pokemon battles.
 *
 * Ensures that trainers follow the item usage limit and
 * applies each selected item to their own Pokemon.
 */
public class ItemManager {


  public void useItem(Trainer trainer, BattleItem item) {
    Objects.requireNonNull(trainer, "Trainer cannot be null.");
    Objects.requireNonNull(item, "Item cannot be null.");

    if (trainer.getPokemon() == null) {
      throw new IllegalStateException(
          "Trainer must have a Pokemon to use an item."
      );
    }

    if (!trainer.canUseItem()) {
      throw new IllegalStateException(
          "Trainer has reached the item usage limit."
      );
    }

    item.use(trainer.getPokemon());
    trainer.registerItemUse();
  }
}