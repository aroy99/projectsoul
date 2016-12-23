
package komorebi.projsoul.engine;
import komorebi.projsoul.script.TextHandler;

import java.util.Scanner;

import komorebi.projsoul.script.EarthboundFont;

public class HUD implements Renderable
{
  public int baseHealth;
  public int health;
  public int money;
  TextHandler text;
  
 //New Bank stuff
  public Bank bank = new Bank(100);
  int count = 0;
  int amount;
  //^

  public HUD(int initHealth, int initmoney)
  {
	money = initmoney;
    baseHealth = health = initHealth; 
    text = new TextHandler();
  }

  @Override
  public void update() {
    // TODO Auto-generated method stub
	  //Updates the player's health as it is lost or gained
	text.clear();
	if(health >= 0)
	{
		text.write(String.valueOf(health), 7, 207, new EarthboundFont(1));
	}
	text.write(String.valueOf(money), 19, 194, new EarthboundFont(1));
	
	//From here on is new Bank code
	//For some reason this doesn't work, it crashes the game? The Scanner perhaps?
	//Currently only works in increments of 10 coins (temporary)
	text.write("Type d for deposit, w for withdrawal",10,100, new EarthboundFont(1));
	if(KeyHandler.keyClick(Key.D))
	{
		System.out.println("Please enter an amount to deposit");
		amount = 10;
		bank.deposit(amount);
	}
	else if(KeyHandler.keyClick(Key.W))
	{
		System.out.println("Please enter an amount to withdraw");
		amount = 10;
		bank.withdraw(amount);
	}
	//^
  }
  @Override
  public void render() {
    // TODO Auto-generated method stub
    //Health bar
    if (health > 0)
    {
      Draw.rect(5, 205, (int) (health*(100 / (double) baseHealth)), 10, 53, 24, 54, 25, 2);
    }
    //Border
    Draw.rect(5, 204, 102, 2, 39, 46, 40, 47, 2);
    Draw.rect(5, 215, 102, 2, 39, 46, 40, 47, 2);
    Draw.rect(5, 205, 2, 10, 39, 46, 40, 47, 2);
    Draw.rect(105, 205, 2, 10, 39, 46, 40, 47, 2);
    
    //Draws the currency symbol and the equals sign
    Draw.rect(5, 193, (float) 8.5, 10, 80, 0, 88, 11, 14);
    Draw.rect(14, 196, 5, 1, 80, 38, 85, 38, 5);
    Draw.rect(14, 198, 5, 1, 80, 38, 85, 38, 5);
    text.render();
  }
 
  public void addToMaxHealth(int add)
  {
    baseHealth += add;
    health += add;
  }
  
  public int getHealth()
  {
    return health;
  }
  
  public void giveMoney(int add)
  {
	money += add;
  }
	  
  public void takeMoney(int subtract)
  {
	money -= subtract;
  }
  
  public int getMoney()
  {
	return money;
  }

}





