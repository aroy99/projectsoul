
package komorebi.projsoul.engine;
import komorebi.projsoul.script.TextHandler;
import komorebi.projsoul.script.EarthboundFont;

public class HUD implements Renderable
{
	public boolean broke;
	public int health;
	public int baseHealth;
	public int wallet;  
	public int magic;
	public int maxMagic;
	double proportion;
	TextHandler text = new TextHandler();
	public int baseMagic = 100;
	static final int BAR_WIDTH = 100;

	public HUD(int initHealth, int initMoney, int initMagic)
	{
		//Possible limit on wallet size?
		wallet = initMoney;
		baseHealth = health = initHealth;
		maxMagic = magic = initMagic;
	}
	@Override
	public void update() 
	{
		//Updates the HUD according to the player's health, currency and magic as it is lost or gained
		text.clear();
		if(health >= 0)
		{
			text.write(String.valueOf(health), 7, 207, new EarthboundFont(1));
		}
		text.write(String.valueOf(wallet), 20, 194, new EarthboundFont(1));

		if(wallet == 0)
			broke = true;
		else
			broke = false;

		proportion = (double) this.magic/maxMagic;
		text.write(String.valueOf(magic), 127, 207, new EarthboundFont(1));
	}
	@Override
	public void render() 
	{
		//Health bar
		if (health > 0)
		{
			Draw.rect(5, 205, (int) (health*(100 / (double) baseHealth)), 10, 53, 24, 54, 25, 2);
		}
		Draw.rect(5, 204, 102, 2, 39, 46, 40, 47, 2);
		Draw.rect(5, 215, 102, 2, 39, 46, 40, 47, 2);
		Draw.rect(5, 205, 2, 10, 39, 46, 40, 47, 2);
		Draw.rect(105, 205, 2, 10, 39, 46, 40, 47, 2);

		//Draws the currency symbol and the equals sign
		Draw.rect(5, 193, (float) 8.5, 10, 80, 0, 88, 11, 14);
		Draw.rect(14, 196, 5, 1, 80, 38, 85, 38, 5);
		Draw.rect(14, 198, 5, 1, 80, 38, 85, 38, 5);

		//Magic Bar
		Draw.rect(125, 205, (int) (proportion*BAR_WIDTH), 10, 41, 22, 42, 23, 2);
		Draw.rect(125, 204, baseMagic+2, 2, 39, 46, 40, 47, 2);
		Draw.rect(125, 215, baseMagic+2, 2, 39, 46, 40, 47, 2);
		Draw.rect(125, 205, 2, 10, 39, 46, 40, 47, 2);
		Draw.rect(baseMagic+125, 205, 2, 10, 39, 46, 40, 47, 2);

		text.render();
	}
	//Checks if the current player's health is full
	public boolean fullHealth()
	{
		if(health == baseHealth)
			return true;
		else
			return false;
	}

	//Checks if current player's magic is full
	public boolean fullMagic()
	{
		if(magic == maxMagic)
			return true;
		else
			return false;
	}
	//Adds a given amount to the max health
	public void addToMaxHealth(int add)
	{
		baseHealth += add;
		health += add;
	}

	//Adds a given amount to health
	public void addHealth(int add)
	{
		health+= add;
	}

	//Returns the amount of health
	public int getHealth()
	{
		return health;
	}

	//Returns the MaxHealth
	public int getMaxHealth()
	{
		return baseHealth;
	}

	//Adds a given amount of money
	public void giveMoney(int add)
	{
		wallet += add;
	}

	//Subtracts a give amount of money
	public void takeMoney(int subtract)
	{
		if(wallet - subtract >= 0)
		{
			wallet -= subtract;
		}
		else
		{
			System.out.println("Not enough cash");
		}
	}

	public boolean hasEnoughMoney(int moneyNeeded)
	{
		return wallet>=moneyNeeded;
	}

	//Returns max magic
	public int getMaxMagic()
	{
		return maxMagic;
	}

	//Returns current amount of magic
	public int getMagic()
	{
		return magic;
	}

	//Returns the amount of money
	public int getMoney()
	{
		return wallet;
	}

	/**
	 * Adjusts the amount of magic left in the magic bar
	 * @param dMagic The change in magic (- to decrease magic)
	 */
	public void changeMagicBy(int dMagic)
	{
		magic += dMagic;

		if (magic<0) magic = 0;
	}

	/**
	 * @param magicNeeded The amount of magic needed to perform an attack/task
	 * @return Whether the magic bar currently has enough magic to perform that
	 * attack/task
	 */
	public boolean hasEnoughMagic(int magicNeeded)
	{
		return magic>=magicNeeded;
	}

	public void addToMaxMagic(int add)
	{
		maxMagic += add;
		magic += add;
	}
}