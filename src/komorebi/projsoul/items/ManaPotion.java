package komorebi.projsoul.items;

import komorebi.projsoul.engine.Inventory;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.MapHandler;

public class ManaPotion extends Potion 
{
  Characters character;

  public ManaPotion(String name, int salePrice, int quantity, int effect) 
  {
    super(name, salePrice, quantity, "Restores magic by " + effect + " points", effect);
  }

  @Override
  public void useItem() 
  {
    character = MapHandler.currentPlayer();
    int dif = Map.getPlayer().getCharacterHUD(character).getMaxMagic() - Map.getPlayer().getCharacterHUD(character).getMagic();
    if(dif > 0 && dif < effect)
    {
      Map.getPlayer().getCharacterHUD(character).changeMagicBy(dif);
      Inventory.removeItem(this);
    }
    else if(Map.getPlayer().getCharacterHUD(character).fullMagic())
    {
      System.out.println("Your magic is full!");
    }
    else
    {
      Map.getPlayer().getCharacterHUD(character).changeMagicBy(effect);
      Inventory.removeItem(this);
    }

  }

}
