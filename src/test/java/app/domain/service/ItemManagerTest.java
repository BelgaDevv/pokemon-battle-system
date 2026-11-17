
package app.domain.service;

import app.domain.enums.ElementalType;
import app.domain.item.BattleItem;
import app.domain.model.Attack;
import app.domain.model.Pokemon;
import app.domain.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemManagerTest {

  private ItemManager itemManager;
  private Trainer trainer;
  private Pokemon pokemon;
  private BattleItem item;

  @BeforeEach
  void setUp() {
    itemManager = new ItemManager();

    Attack attack = new Attack("Tackle", 30, 100, 10, List.of());

    pokemon = new Pokemon(
        "CharSal", ElementalType.FIRE,
        100, 50, 40, 30, List.of(attack)
    );

    trainer = new Trainer("Trainer 1");
    trainer.setPokemon(pokemon);

    item = mock(BattleItem.class);
  }

  // Applies the item to the trainer's Pokemon.
  @Test
  void shouldUseItemOnTrainerPokemon() {
    itemManager.useItem(trainer, item);

    verify(item).use(pokemon);
  }

  // Registers item usage and respects the usage limit.
  @Test
  void shouldRegisterItemUsage() {
    itemManager.useItem(trainer, item);
    assertTrue(trainer.canUseItem());

    itemManager.useItem(trainer, item);
    assertFalse(trainer.canUseItem());
  }


  // Allows the trainer to use two items.
  @Test
  void shouldAllowTwoItemUses() {
    itemManager.useItem(trainer, item);
    itemManager.useItem(trainer, item);

    verify(item, times(2)).use(pokemon);
  }

  // Rejects item usage after reaching the limit.
  @Test
  void shouldRejectItemUsageAfterLimit() {
    itemManager.useItem(trainer, item);
    itemManager.useItem(trainer, item);

    assertThrows(
        IllegalStateException.class,
        () -> itemManager.useItem(trainer, item)
    );

    verify(item, times(2)).use(pokemon);
  }

  // Does not register usage when the item fails.
  @Test
  void shouldNotRegisterUsageWhenItemFails() {
    doThrow(new IllegalStateException("Item usage failed."))
        .when(item).use(pokemon);

    assertThrows(
        IllegalStateException.class,
        () -> itemManager.useItem(trainer, item)
    );

    assertTrue(trainer.canUseItem());
  }

  // Rejects a null trainer.
  @Test
  void shouldRejectNullTrainer() {
    assertThrows(
        NullPointerException.class,
        () -> itemManager.useItem(null, item)
    );
  }

  // Rejects a null item.
  @Test
  void shouldRejectNullItem() {
    assertThrows(
        NullPointerException.class,
        () -> itemManager.useItem(trainer, null)
    );
  }

  // Rejects a trainer without a Pokemon.
  @Test
  void shouldRejectTrainerWithoutPokemon() {
    Trainer trainerWithoutPokemon = new Trainer("Trainer 2");

    assertThrows(
        IllegalStateException.class,
        () -> itemManager.useItem(trainerWithoutPokemon, item)
    );

    verifyNoInteractions(item);
  }

  // Rejects item usage when the trainer has reached the limit.
  @Test
  void shouldNotUseItemWhenLimitIsReached() {
    itemManager.useItem(trainer, item);
    itemManager.useItem(trainer, item);

    assertThrows(
        IllegalStateException.class,
        () -> itemManager.useItem(trainer, item)
    );

    verify(item, times(2)).use(pokemon);
  }
}
