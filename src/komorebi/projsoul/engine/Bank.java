package komorebi.projsoul.engine;

import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.map.Map;

public class Bank implements Renderable 
{
	public static int balance;
	public Characters character;
	public int characterCash;
	
	public Bank(int bal)
	{
		balance = bal;
	}
	
	public void deposit(int amount)
	{
		character = Map.currentPlayer();
	    characterCash = Map.getPlayer().getCharacterHUD(character).getMoney();
	    
	    if(characterCash < amount)
	    {
	    	System.out.println("You don't have enough money fool");
	    }
	    else
	    {
	    	Map.getPlayer().getCharacterHUD(character).takeMoney(amount);
	    	balance+=amount;
	    }
	    System.out.println(balance);
	}
	
	public void withdraw(int amount)
	{
	    if(balance < amount)
	    {
	    	System.out.println("Your bank is empty fool");
	    }
	    else
	    {
	    	character = Map.currentPlayer();
	    	Map.getPlayer().getCharacterHUD(character).giveMoney(amount);
	    	balance-=amount;
	    }
	    System.out.println(balance);
	}
	
	public int getBalance()
	{
		return balance;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

}
