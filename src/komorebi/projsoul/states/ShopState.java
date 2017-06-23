package komorebi.projsoul.states;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.engine.Shop;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.gameplay.HUD;
import komorebi.projsoul.script.text.EarthboundFont;
import komorebi.projsoul.script.text.TextHandler;

public class ShopState extends State 
{
  private int count = 0;
  private TextHandler text = new TextHandler();
  EarthboundFont font = new EarthboundFont(1);
  public static InventoryState inventory = new InventoryState();
  boolean buy;
  static boolean sell = false;
  private int index = 0;
  private int index2 = 0;
  Shop shop = new Shop("Armor Shop", 11);
  int count2 = 50;
  int count3 = 140;
  int topindex;
  int bottomindex = 5;
  public Characters character;
  public HUD healths = new HUD(0,0,0);

  public ShopState()
  {

  }

  @Override
  public void getInput() 
  {
    if(count == 0)
    {
      shop.addShopItem("Health Potion");
      shop.addShopItem("Mana Potion");
      shop.addShopItem("MaxHealth Boost");
      shop.addShopItem("XP Boost");
      shop.addShopItem("Level Up Potion");
      shop.addShopItem("Iron Helmet");
      shop.addShopItem("Boots");
      shop.addShopItem("Chestplate");
      shop.addShopItem("Pants");
      shop.addShopItem("Gauntlets");
      //shop.addShopItem("Leather Pants");
      //shop.addShopItem("Steel Chestplate");
      //shop.addShopItem("Super Duper MaxHealth Boost");
      shop.addShopItem("Shitty Helmet");
      count++;
    }

  }

  @Override
  public void update() 
  {

    if(KeyHandler.keyClick(Key.ENTER))
    {
      if(buy)
      {
        if(index == 5)
        {
          topindex = 0;
          bottomindex = 5;
          index2 = 0;
          buy = false;
        }
        else shop.sellItem(shop.getShopItem(index2), 1);
      }

      if(!buy && !sell)
      {
        switch(index)
        {
          case 0:
            buy = true;
            index = 0;
            break;
          case 1:
            sell = true;
            break;
          case 2:
            buy = false;
            sell = false;
            index = 0;
            GameHandler.switchState(States.GAME);
            break;
          default:
            break;	
        }
      }
    }
    text.clear();
    if(!buy && !sell)
    {
      text.write("General Shop", 100, 155, font);
      text.write("Would you like to buy or sell items?", 50, 140, font);
      text.write("Buy", 65, 110, font);
      text.write("Sell", 170, 110, font);
      text.write("Exit", 117, 90, font);
    }

    if(buy)
    {
      text.write("General Shop", 100, 155, font);
      text.write("Exit", 150, 90, font);

      if(shop.items.length<=5)
      {
        topindex = 0;
        bottomindex = shop.items.length;
        count3 = 140;
      }

      for(int i=topindex; i<bottomindex; i++)
      {
        if(count3 == 90)
        {
          count3 = 140;
        }
        text.write(String.valueOf(shop.items[i]), count2, count3, font);
        count3-=10;
      }
    }

    if(sell)
    {
      inventory.refreshInventory();
      inventory.update();
    }

    if(KeyHandler.keyClick(Key.RIGHT))
    {
      if(!buy && !sell)
      {
        index++;
      }
    }

    if(KeyHandler.keyClick(Key.LEFT))
    {
      if(!buy && !sell)
      {
        index--;
      }
    }

    if(KeyHandler.keyClick(Key.DOWN))
    {
      if(buy)
      {
        index2++;
        topindex++;
        bottomindex++;
        if(bottomindex>shop.items.length)
        {
          if(bottomindex == 0)
          {
            index = 5;
          }
          else
          {
            index++;
            topindex = shop.items.length - 5;
            bottomindex = shop.items.length;
          }
        }

      }
    }
    if(KeyHandler.keyClick(Key.UP))
    {
      if(buy)
      {
        index2--;
        if(index != 5)
        {
          topindex--;
          bottomindex--;
        }
        if(index == 5)index = 4;
        if(topindex<0)
        {
          index--;
          topindex = 0;
          bottomindex = 5;
        }

      }
    }

    if(KeyHandler.keyClick(Key.W))
    {
      GameHandler.switchState(States.GAME);
    }
  }

  @Override
  public void render() 
  {
    Draw.rect(10, 75, 240, 100, 0, 0, 220, 59, 6);

    if(!buy && !sell)
    {
      //Index marker
      if(index == 0)Draw.rect(62, 113, 2, 2, 48, 3, 50, 5, 5);
      else if(index == 1)Draw.rect(167, 113, 2, 2, 48, 3, 50, 5, 5);
      else if(index == 2)Draw.rect(114, 93, 2, 2, 48, 3, 50, 5,5);

      //Check
      if(index > 2)index = 0;
      else if(index < 0) index = 2;
    }

    if(buy)
    {
      if(index > 5)index = 5;

      if(shop.items.length > 4 && index > 4)
        index = 5;
      else
        if(index > shop.items.length)index = shop.items.length;

      if(index < 0)index = 0;

      if(index <= 0)Draw.rect(27, 142, 2, 2, 48, 3, 50, 5, 5);
      else if(index == 1)Draw.rect(27, 132, 2, 2, 48, 3, 50, 5, 5);
      else if(index == 2)Draw.rect(27, 122, 2, 2, 48, 3, 50, 5,5);
      else if(index == 3)Draw.rect(27, 112, 2, 2, 48, 3, 50, 5, 5);
      else if(index == 4)Draw.rect(27, 102, 2, 2, 48, 3, 50, 5,5);
      else if(index == 5)Draw.rect(147, 92, 2, 2, 48, 3, 50, 5,5);

      if(index2 < 0)index2 = 0;
      if(index2 > shop.items.length)index2 = shop.items.length;
    }

    if(sell)
    {
      inventory.render();
    }



    text.render();

  }

}
