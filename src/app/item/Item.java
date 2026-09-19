package app.item;

/**
 * Represents an item that can be used during a battle.
 */
public class Item {

  private String name;
  private ItemEffect itemEffect;

  /**
   * Creates an item with the specified effect and name.
   */
  public Item(ItemEffect itemEffect, String name) {
    this.itemEffect = itemEffect;
    this.name = name;
  }

  /**
   * Returns the effect associated with this item.
   */
  public ItemEffect getItemEffect() {
    return itemEffect;
  }

  /**
   * Returns the name of this item.
   */
  public String getName() {
    return name;
  }
}