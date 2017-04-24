package komorebi.projsoul.states;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.script.EarthboundFont;
import komorebi.projsoul.script.TextHandler;
import komorebi.projsoul.engine.Shop;


public class ShopState extends State 
{
	private int count = 0;
	private TextHandler text = new TextHandler();
	EarthboundFont font = new EarthboundFont(1);
	private int index = 0;
	Shop armorShop = new Shop("Armor Shop", 8);
	int count2 = 27;
	int count3 = 143;

	
	public ShopState()
	{
		
	}
	
	@Override
	public void getInput() 
	{
		if(count == 0)
		{
			armorShop.addShopItem("Health Potion");
			armorShop.addShopItem("Mana Potion");
			armorShop.addShopItem("MaxHealth Boost");
			armorShop.addShopItem("Helmet");
			armorShop.addShopItem("Boots");
			armorShop.addShopItem("Chestplate");
			armorShop.addShopItem("Pants");
			armorShop.addShopItem("Gauntlets");
			count++;
		}

	}

	@Override
	public void update() 
	{
		text.clear();
		text.write("General Shop", 100, 155, font);
		if(KeyHandler.keyClick(Key.DOWN))
		{
			index++;
		}
		if(KeyHandler.keyClick(Key.UP))
		{
			index--;
		}
		
		if(KeyHandler.keyClick(Key.ENTER))
		{
			armorShop.sellItem(armorShop.getShopItem(index), 1);
		}
		if(KeyHandler.keyClick(Key.V))
		{
			GameHandler.switchState(States.GAME);
		}
		//Perhaps make it so each shop has its own render and update method?
		//So the shop state is universal?
		text.write("Health Potion", 30, 140, font);
		text.write("Mana Potion", 30, 130, font);
		text.write("MaxHealth Boost", 30, 120, font);
		text.write("Helmet", 30, 110, font);
		text.write("Boots", 30, 100, font);
		text.write("Chestplate", 30, 90, font);
		text.write("Pants", 110, 140, font);
		text.write("Gaunlets", 110, 130, font);
		
	}

	@Override
	public void render() 
	{
		Draw.rect(10, 75, 240, 100, 0, 0, 220, 59, 6);
		
		if(index == 0)Draw.rect(count2, count3, 2, 2, 48, 3, 50, 5, 5);
		else if(index == 1)Draw.rect(count2, count3-10, 2, 2, 48, 3, 50, 5, 5);
		else if(index == 2)Draw.rect(count2, count3-20, 2, 2, 48, 3, 50, 5,5);
		else if(index == 3)Draw.rect(count2, count3-30, 2, 2, 48, 3, 50, 5, 5);
		else if(index == 4)Draw.rect(count2, count3-40, 2, 2, 48, 3, 50, 5,5);
		else if(index == 5)Draw.rect(count2, count3-50, 2, 2, 48, 3, 50, 5, 5);
		else if(index == 6)Draw.rect(count2+77, count3, 2, 2, 48, 3, 50, 5, 5);
		else if(index == 7)Draw.rect(count2+77, count3-10, 2, 2, 48, 3, 50, 5, 5);
		
		
		if(index > 7)index = 0;
		else if(index < 0) index = 7;
		
		text.render();

	}

}
