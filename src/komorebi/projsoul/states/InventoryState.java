package komorebi.projsoul.states;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.Inventory;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.gameplay.HUD;
import komorebi.projsoul.items.Armor;
import komorebi.projsoul.items.Boots;
import komorebi.projsoul.items.CharacterItem;
import komorebi.projsoul.items.Chestplate;
import komorebi.projsoul.items.Consumable;
import komorebi.projsoul.items.Gauntlets;
import komorebi.projsoul.items.Helmet;
import komorebi.projsoul.items.Pants;
import komorebi.projsoul.items.Ring;
import komorebi.projsoul.map.MapHandler;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.TextHandler;

public class InventoryState extends State 
{
  private TextHandler text = new TextHandler();
  EarthboundFont font = new EarthboundFont(1);
  int index, index2 = 0;
  private int topindex = 0;
  public static ShopState shop = new ShopState();
  private int bottomindex = 5;
  private int count = 50;
  private int count2 = 140;
  private int count4;
  private static int count3 = 0;
  private static CharacterItem[] items;
  public Characters character;
  public HUD healths = new HUD(0,0,0);

  public void getInput() 
  {
  }

  public void update() 
  {	
    refreshInventory();
    text.clear();
    text.write("Inventory", 100, 155, font);
    text.write("Exit", 150, 90, font);
    if(KeyHandler.keyClick(Key.DOWN))
    {	
      index2++;
      topindex++;
      bottomindex++;

      if(bottomindex>items.length)
      {
        if(bottomindex == 0)index = 5;
        else
        {
          index++;
          topindex = items.length - 5;
          bottomindex = items.length;
        }
      }
      if(index2 > items.length)index2 = items.length-1;

    }
    if(KeyHandler.keyClick(Key.UP))
    {
      index2--;
      if(index != 5)
      {
        topindex--;
        bottomindex--;
      }
      else if(items.length < 5)index = items.length-1;
      else index = 4;
      if(topindex<0)
      {
        index--;
        topindex = 0;
        bottomindex = 5;
      }
      if(index2 < 0)index2 = 0;
    }

    if(KeyHandler.keyClick(Key.U))
      GameHandler.switchState(States.GAME);


    if(KeyHandler.keyClick(Key.E))
    {
      if(!ShopState.sell)
      {
        if(index != 5)
        {
          if(Inventory.getInventoryItem(index2) instanceof Armor)
          {
            Armor a = (Armor) Inventory.getInventoryItem(index2);
            if(a.equipped)a.unequip();
            else a.equip();
            //Checks that no more than 1 of each armor piece is equipped at a time
            for(int i=0; i<items.length; i++)
            {
              if(Inventory.getInventoryItem(i) instanceof Helmet)
              {
                if(a instanceof Helmet && Inventory.getInventoryItem(i) != a)
                {
                  if(a.equipped)
                  {
                    Armor b = (Armor) Inventory.getInventoryItem(i);
                    if(b.getEquippedCharacter() == a.getEquippedCharacter())
                    {
                      if(b.equipped)b.unequip();
                    }
                  }
                }
              }
              else if(Inventory.getInventoryItem(i) instanceof Chestplate)
              {
                if(a instanceof Chestplate && Inventory.getInventoryItem(i) != a)
                {
                  if(a.equipped)
                  {
                    Armor b = (Armor) Inventory.getInventoryItem(i);
                    if(b.getEquippedCharacter() == a.getEquippedCharacter())
                    {
                      if(b.equipped)b.unequip();
                    }
                  }
                }
              }
              else if(Inventory.getInventoryItem(i) instanceof Gauntlets)
              {
                if(a instanceof Gauntlets && Inventory.getInventoryItem(i) != a)
                {
                  if(a.equipped)
                  {
                    Armor b = (Armor) Inventory.getInventoryItem(i);
                    if(b.getEquippedCharacter() == a.getEquippedCharacter())
                    {
                      if(b.equipped)b.unequip();
                    }
                  }
                }
              }
              else if(Inventory.getInventoryItem(i) instanceof Pants)
              {
                if(a instanceof Pants && Inventory.getInventoryItem(i) != a)
                {
                  if(a.equipped)
                  {
                    Armor b = (Armor) Inventory.getInventoryItem(i);
                    if(b.getEquippedCharacter() == a.getEquippedCharacter())
                    {
                      if(b.equipped)b.unequip();
                    }
                  }
                }
              }
              else if(Inventory.getInventoryItem(i) instanceof Boots)
              {
                if(a instanceof Boots && Inventory.getInventoryItem(i) != a)
                {
                  if(a.equipped)
                  {
                    Armor b = (Armor) Inventory.getInventoryItem(i);
                    if(b.getEquippedCharacter() == a.getEquippedCharacter())
                    {
                      if(b.equipped)b.unequip();
                    }
                  }
                }
              }
              else if(Inventory.getInventoryItem(i) instanceof Ring)
              {
                if(a instanceof Ring && Inventory.getInventoryItem(i) != a)
                {
                  if(a.equipped)
                  {
                    Armor b = (Armor) Inventory.getInventoryItem(i);
                    if(b.getEquippedCharacter() == a.getEquippedCharacter())
                    {
                      if(b.equipped)b.unequip();
                    }
                  }
                }
              }

            }
          }
          else if(Inventory.getInventoryItem(index2) instanceof Consumable)
          {
            Consumable c = (Consumable) Inventory.getInventoryItem(index2);
            c.useItem();
            if(Inventory.checkRemoved() && bottomindex == items.length)
            {
              bottomindex--;
              topindex--;
              index2--;
            }
          }
        }
      }

      if(index == 5)
      {
        if(ShopState.sell)
        {
          index2 = 0;
          index = 0;
          ShopState.sell = false;
        }
        else
        {
          if(items.length < 5)bottomindex = items.length-1;
          else bottomindex = 5;
          topindex = 0;
          index2 = 0;
          index = 0;
          GameHandler.switchState(States.GAME);
        }
      }
      else
      {
        if(ShopState.sell)
        {
          character = MapHandler.currentPlayer();
          healths = MapHandler.getPlayer().getCharacterHUD(character);
          healths.giveMoney(Inventory.getInventoryItem(index2).getResalePrice());
          Inventory.removeItem(Inventory.getInventoryItem(index2), index2);
          refreshInventory();
          if(Inventory.checkRemoved())
          {
            //Under Revision!!!!
            //if(index!=index2 || index2!=items.length-1)index2--;
            if(index2>5 && index2!=items.length-1)index2--;
            if(topindex!=0)
            {
              topindex--;
              bottomindex--;
            }

          }

        }
      }
      refreshInventory();
    }

    if(items.length<=5)
    {
      topindex = 0;
      bottomindex = items.length;
      count2 = 140;
    }

    for(int i=topindex; i<bottomindex; i++)
    {
      if(count2 == 90)count2 = 140;
      //It prints it out in the correct order however it does not display in the correct order?!
      //(Specific situation: less than 5 come back more than 5, explosions/spazzes)
      //System.out.println(items[i].getName());
      text.write(String.valueOf(items[i].getName()), count, count2, font);
      text.write(String.valueOf(items[i].getQuantity()), count+80, count2, font);
      text.write(String.valueOf(items[i].getResalePrice()), count+90, count2, font);
      count2-=10;
    }	
    if(items.length>=1)
    {
      for(int i=topindex; i<bottomindex; i++)
      {
        if(Inventory.getInventoryItem(i) instanceof Armor)
        {
          Armor a = (Armor) Inventory.getInventoryItem(i);
          if(a.checkEquipped())
          {
            if(i == topindex)text.write("E", count+105, 140, font);
            else if(i == topindex+1)text.write("E", count+105, 130, font);
            else if(i == topindex+2)text.write("E", count+105, 120, font);
            else if(i == topindex+3)text.write("E", count+105, 110, font);
            else text.write("E", count+105, 100, font);
          }
          if(a.checkBroken())
          {
            if(i == topindex)text.write("Broken", count+115, 140, font);
            else if(i == topindex+1)text.write("Broken", count+115, 130, font);
            else if(i == topindex+2)text.write("Broken", count+115, 120, font);
            else if(i == topindex+3)text.write("Broken", count+115, 110, font);
            else text.write("Broken", count+115, 100, font);
          }
          else
          {
            if(i == topindex)text.write(String.valueOf(a.getDurability())+" /" +String.valueOf(a.maxDurability), count+115, 140, font);
            else if(i == topindex+1)text.write(String.valueOf(a.getDurability())+" /" +String.valueOf(a.maxDurability), count+115, 130, font);
            else if(i == topindex+2)text.write(String.valueOf(a.getDurability())+" /" +String.valueOf(a.maxDurability), count+115, 120, font);
            else if(i == topindex+3)text.write(String.valueOf(a.getDurability())+" /" +String.valueOf(a.maxDurability), count+115, 110, font);
            else text.write(String.valueOf(a.getDurability())+" /" +String.valueOf(a.maxDurability), count+115, 100, font);
          }
        }
      }
    }				
  }


  public void render() 
  {
    Draw.rect(10, 75, 240, 100, 0, 0, 220, 59, 6);

    if(index > 5)index = 5;
    if(items.length < 5 && index == items.length)index = 5;
    if(index < 0)index = 0;

    if(index == 0)Draw.rect(27, 142, 2, 2, 48, 3, 50, 5, 5);
    else if(index == 1)Draw.rect(27, 132, 2, 2, 48, 3, 50, 5, 5);
    else if(index == 2)Draw.rect(27, 122, 2, 2, 48, 3, 50, 5,5);
    else if(index == 3)Draw.rect(27, 112, 2, 2, 48, 3, 50, 5, 5);
    else if(index == 4)Draw.rect(27, 102, 2, 2, 48, 3, 50, 5,5);
    else if(index == 5)Draw.rect(147, 92, 2, 2, 48, 3, 50, 5,5);

    text.render();

  }
  public void refreshInventory()
  {
    count3 = 0;
    items = new CharacterItem[Inventory.numOfItems];
    for(CharacterItem item: Inventory.items)
    {
      if(count3 == items.length)break;
      items[count3] = item;
      count3++;

    }		
  }
}
