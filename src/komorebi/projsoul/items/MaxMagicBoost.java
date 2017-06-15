package komorebi.projsoul.items;

import komorebi.projsoul.engine.Inventory;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.MapHandler;

public class MaxMagicBoost extends Potion
{
  Characters character;

  public MaxMagicBoost(String name, int salePrice, int quantity, String effectDescription, int effect) 
  {
    super(name, salePrice, quantity, "Boosts max magic permenantly by " + effect + " points", effect);
  }


  public void useItem() 
  {
    character = MapHandler.currentPlayer();
    Map.getPlayer().getCharacterHUD(character).addToMaxMagic(effect);
    Inventory.removeItem(this);
  }

}
