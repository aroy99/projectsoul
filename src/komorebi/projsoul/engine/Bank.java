package komorebi.projsoul.engine;

import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.map.MapHandler;

public class Bank 
{
  public static int balance;
  public Characters character;
  public int characterCash;
  public static final int max = 999999;

  public Bank(int bal)
  {
    balance = bal;
  }

  public void deposit(int amount)
  {
    character = MapHandler.currentPlayer();
    characterCash = Map.getPlayer().getCharacterHUD(character).getMoney();
    if(balance == max)
    {
      balance = max;
    }
    else
    {
      Map.getPlayer().getCharacterHUD(character).takeMoney(amount);
      balance+=amount;
    }
  }

  public void withdraw(int amount)
  {
    //Perhaps a limit on the wallet?
    {
      character = MapHandler.currentPlayer();
      MapHandler.getPlayer().getCharacterHUD(character).giveMoney(amount);
      balance-=amount;
    }
  }

  public int getBalance()
  {
    return balance;
  }
}
