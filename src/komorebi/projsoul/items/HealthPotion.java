package komorebi.projsoul.items;

import komorebi.projsoul.engine.Inventory;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.MapHandler;

public class HealthPotion extends Potion 
{
  Characters character;

  public HealthPotion(String name, int salePrice, int quantity, int effect) 
  {
    super(name, salePrice, quantity, "Heal by " + effect + " points", effect);

  }

  public void useItem() 
  {
    character = MapHandler.currentPlayer();
    int dif = Map.getPlayer().getCharacterHUD(character).getMaxHealth() - Map.getPlayer().getCharacterHUD(character).getHealth();
    if(dif > 0 && dif < effect)
    {
      Map.getPlayer().getCharacterHUD(character).addHealth(dif);
      Inventory.removeItem(this);
    }
    else if(Map.getPlayer().getCharacterHUD(character).fullHealth())
    {
      System.out.println("Your health is full!");
    }
    else
    {
      Map.getPlayer().getCharacterHUD(character).addHealth(effect);
      Inventory.removeItem(this);
    }

  }

}
