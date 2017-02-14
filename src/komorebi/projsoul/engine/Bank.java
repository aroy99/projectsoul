package komorebi.projsoul.engine;

import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.map.Map;

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
		character = Map.currentPlayer();
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
	    {
	    	character = Map.currentPlayer();
	    	Map.getPlayer().getCharacterHUD(character).giveMoney(amount);
	    	balance-=amount;
	    }
	}
	
	public int getBalance()
	{
		return balance;
	}
}
