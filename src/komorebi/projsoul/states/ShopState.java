package komorebi.projsoul.states;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.HUD;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.script.EarthboundFont;
import komorebi.projsoul.script.TextHandler;
import komorebi.projsoul.engine.Shop;
import komorebi.projsoul.entities.Characters;

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
	Shop armorShop = new Shop("Armor Shop", 12);
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
			armorShop.addShopItem("Health Potion");
			armorShop.addShopItem("Mana Potion");
			armorShop.addShopItem("MaxHealth Boost");
			armorShop.addShopItem("Iron Helmet");
			armorShop.addShopItem("Boots");
			armorShop.addShopItem("Chestplate");
			armorShop.addShopItem("Pants");
			armorShop.addShopItem("Gauntlets");
			armorShop.addShopItem("Leather Pants");
			armorShop.addShopItem("Steel Chestplate");
			armorShop.addShopItem("Super Duper MaxHealth Boost");
			armorShop.addShopItem("Shitty Helmet");
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
				else armorShop.sellItem(armorShop.getShopItem(index2), 1);
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
			
			if(armorShop.items.length<=5)
			{
				topindex = 0;
				bottomindex = armorShop.items.length;
				count3 = 140;
			}
			
			for(int i=topindex; i<bottomindex; i++)
			{
				if(count3 == 90)
				{
					count3 = 140;
				}
				text.write(String.valueOf(armorShop.items[i]), count2, count3, font);
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
				if(bottomindex>armorShop.items.length)
				{
					if(bottomindex == 0)
					{
						index = 5;
					}
					else
					{
						index++;
						topindex = armorShop.items.length - 5;
						bottomindex = armorShop.items.length;
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
		
		if(KeyHandler.keyClick(Key.V))
		{
			GameHandler.switchState(States.GAME);
		}
		//Perhaps make it so each shop has its own render and update method?
		//So the shop state is universal?
		
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
			
			if(armorShop.items.length > 4 && index > 4)
				index = 5;
			else
				if(index > armorShop.items.length)index = armorShop.items.length;
			
			if(index < 0)index = 0;
			
			if(index <= 0)Draw.rect(27, 142, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 1)Draw.rect(27, 132, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 2)Draw.rect(27, 122, 2, 2, 48, 3, 50, 5,5);
			else if(index == 3)Draw.rect(27, 112, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 4)Draw.rect(27, 102, 2, 2, 48, 3, 50, 5,5);
			else if(index == 5)Draw.rect(147, 92, 2, 2, 48, 3, 50, 5,5);
			
			if(index2 < 0)index2 = 0;
			if(index2 > armorShop.items.length-1)index2 = armorShop.items.length-1;
		}
		
		if(sell)
		{
			inventory.render();
		}
		
		
		
		text.render();

	}

}
