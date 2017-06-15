package komorebi.projsoul.engine;

import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.items.Boots;
import komorebi.projsoul.items.Chestplate;
import komorebi.projsoul.items.Gauntlets;
import komorebi.projsoul.items.HealthPotion;
import komorebi.projsoul.items.Helmet;
import komorebi.projsoul.items.LevelUpPotion;
import komorebi.projsoul.items.ManaPotion;
import komorebi.projsoul.items.MaxHealthBoost;
import komorebi.projsoul.items.Pants;
import komorebi.projsoul.items.XPBoost;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.MapHandler;

public class Shop 
{
  public String name;
  public String[] items;
  public int numOfItems;
  public int count;
  Characters character;

  public Shop(String name, int numOfItems)
  {
    this.name = name;
    this.numOfItems = numOfItems;
    items = new String[numOfItems];
    count = 0;
  }

  public void addShopItem(String item)
  {
    items[count]=(item);
    count++;
  }

  public String getShopItem(int index)
  {
    return items[index];
  }

  public int getNumItems()
  {
    return numOfItems;
  }

  public void sellItem(String item, int numSold)
  {
    for(String myItem: items)
    {
      if(item == myItem)
      {
        if(myItem.indexOf("Health Potion")!=-1)
        {
          HealthPotion potion = new HealthPotion("Health Potion", 10, numSold, 20);
          character = MapHandler.currentPlayer();
          if(MapHandler.getPlayer().getCharacterHUD(character).hasEnoughMoney(10))
          {
            Inventory.addItem(potion);
            MapHandler.getPlayer().getCharacterHUD(character).takeMoney(numSold*10);
            System.out.println(numSold + " health potion(s) were added to your inventory");
          }
        }
        else if(myItem.indexOf("Mana Potion")!=-1)
        {
          ManaPotion potion = new ManaPotion("Mana Potion", 15, numSold, 15);
          character = MapHandler.currentPlayer();
          if(Map.getPlayer().getCharacterHUD(character).hasEnoughMoney(15))
          {
            Inventory.addItem(potion);
            Map.getPlayer().getCharacterHUD(character).takeMoney(numSold*15);
            System.out.println(numSold + " mana potion(s) were added to your inventory");
          }
        }
        else if(myItem.indexOf("MaxHealth Boost")!=-1)
        {
          MaxHealthBoost potion = new MaxHealthBoost("MaxHealth Boost", 50, numSold, 10);
          character = MapHandler.currentPlayer();
          if(Map.getPlayer().getCharacterHUD(character).hasEnoughMoney(50))
          {
            Inventory.addItem(potion);
            Map.getPlayer().getCharacterHUD(character).takeMoney(numSold*50);
            System.out.println(numSold + " maxhealth boost(s) were added to your inventory");
          }
        }
        else if(myItem.indexOf("Iron Helmet")!=-1)
        {
          Helmet helm = new Helmet("Iron Helmet",60,numSold,8,5,"An sturdy, well forged Iron Helmet");
          character = MapHandler.currentPlayer();
          if(Map.getPlayer().getCharacterHUD(character).hasEnoughMoney(60))
          {
            Inventory.addItem(helm);
            Map.getPlayer().getCharacterHUD(character).takeMoney(numSold*60);
            System.out.println(numSold + " Helmet(s) were added to your inventory");
          }
        }
        else if(myItem.indexOf("Boots")!=-1)
        {
          Boots boots = new Boots("Iron Boots",20,numSold,4,20,"A sturdy, well forged pair of boots");
          character = MapHandler.currentPlayer();
          if(Map.getPlayer().getCharacterHUD(character).hasEnoughMoney(20))
          {
            Inventory.addItem(boots);
            Map.getPlayer().getCharacterHUD(character).takeMoney(numSold*20);
            System.out.println(numSold + " Pairs of boots were added to your inventory");
          }
        }
        else if(myItem.indexOf("Chestplate")!=-1)
        {
          Chestplate chest = new Chestplate("Chestplate",40,numSold,6,30,"A basic reinforced tunic");
          character = MapHandler.currentPlayer();
          if(Map.getPlayer().getCharacterHUD(character).hasEnoughMoney(40))
          {
            Inventory.addItem(chest);
            Map.getPlayer().getCharacterHUD(character).takeMoney(numSold*40);
            System.out.println(numSold + " Chestplate(s) were added to your inventory");
          }
        }
        else if(myItem.indexOf("Gauntlets")!=-1)
        {
          Gauntlets guantlet = new Gauntlets("Guantlets",25,numSold,7,40,"Reinforced leather gauntlets");
          character = MapHandler.currentPlayer();
          if(Map.getPlayer().getCharacterHUD(character).hasEnoughMoney(25))
          {
            Inventory.addItem(guantlet);
            Map.getPlayer().getCharacterHUD(character).takeMoney(numSold*25);
            System.out.println(numSold + " Pairs of gauntlets were added to your inventory");
          }
        }
        else if(myItem.indexOf("Pants")!=-1)
        {
          Pants pants = new Pants("Pants",10,numSold,3,15,"Average cotton pants");
          character = MapHandler.currentPlayer();
          if(Map.getPlayer().getCharacterHUD(character).hasEnoughMoney(10))
          {
            Inventory.addItem(pants);
            Map.getPlayer().getCharacterHUD(character).takeMoney(numSold*10);
            System.out.println(numSold + " Pants were added to your inventory");
          }
        }
        else if(myItem.indexOf("Shitty Helmet")!=-1)
        {
          Helmet helm = new Helmet("Shitty Helmet",2,numSold,1,5,"A shitty Helmet");
          character = MapHandler.currentPlayer();
          if(Map.getPlayer().getCharacterHUD(character).hasEnoughMoney(60))
          {
            Inventory.addItem(helm);
            Map.getPlayer().getCharacterHUD(character).takeMoney(numSold*60);
            System.out.println(numSold + " Shitty Helmet(s) were added to your inventory");
          }
        }
        else if(myItem.indexOf("XP Boost")!=-1)
        {
          XPBoost boost = new XPBoost("XPBoost",30,numSold, "Drops 3 boosts with 15 XP", 15);
          character = MapHandler.currentPlayer();
          if(Map.getPlayer().getCharacterHUD(character).hasEnoughMoney(30))
          {
            Inventory.addItem(boost);
            Map.getPlayer().getCharacterHUD(character).takeMoney(numSold*30);
            System.out.println(numSold + " XP Boost(s) were added to your inventory");
          }
        }
        else if(myItem.indexOf("Level Up Potion")!=-1)
        {
          LevelUpPotion level = new LevelUpPotion("Level Up Potion",70,numSold, "Drops 1 boost with enough XP to level up your character once", 0);
          character = MapHandler.currentPlayer();
          if(Map.getPlayer().getCharacterHUD(character).hasEnoughMoney(70))
          {
            Inventory.addItem(level);
            Map.getPlayer().getCharacterHUD(character).takeMoney(numSold*70);
            System.out.println(numSold + " Level Up Potion(s) were added to your inventory");
          }
        }
      }
    }

  }
}
