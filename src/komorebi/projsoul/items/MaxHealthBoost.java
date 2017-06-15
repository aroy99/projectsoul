package komorebi.projsoul.items;

import komorebi.projsoul.engine.Inventory;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.MapHandler;

public class MaxHealthBoost extends Potion
{
  Characters character;

  public MaxHealthBoost(String name, int salePrice, int quantity, int effect) 
  {
    super(name, salePrice, quantity, "Boosts max health permenantly by " + effect + " points", effect);

  }

  @Override
  public void useItem() 
  {
    character = MapHandler.currentPlayer();
    Map.getPlayer().getCharacterHUD(character).addToMaxHealth(effect);
    Inventory.removeItem(this);

  }

}
