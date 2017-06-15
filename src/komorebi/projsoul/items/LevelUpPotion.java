package komorebi.projsoul.items;

import komorebi.projsoul.engine.Inventory;
import komorebi.projsoul.entities.BaseXPObject;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.MapHandler;

public class LevelUpPotion extends Potion 
{
  Characters character;

  public LevelUpPotion(String name, int salePrice, int quantity, String effectDescription, int effect) 
  {
    super(name, salePrice, quantity, "When used it levels up the current character one full level", effect);
  }

  public void useItem() 
  {
    character = Map.getPlayer().getCharacter();
    effect = Player.getXPToNextLevel(character);
    MapHandler.addXPObject(new BaseXPObject(Map.getPlayer().getX()+25, Map.getPlayer().getY(), effect));
    Inventory.removeItem(this);
  }

}
