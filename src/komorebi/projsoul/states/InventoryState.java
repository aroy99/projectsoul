package komorebi.projsoul.states;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.HUD;
import komorebi.projsoul.engine.Inventory;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.items.CharacterItem;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.EarthboundFont;
import komorebi.projsoul.script.TextHandler;
import komorebi.projsoul.states.State.States;

public class InventoryState extends State 
{
	private TextHandler text = new TextHandler();
	EarthboundFont font = new EarthboundFont(1);
	int index = 0;
	private int topindex = 0;
	public static ShopState shop = new ShopState();
	private int bottomindex = 5;
	private int count = 50;
	private int count2 = 140;
	private static int count3 = 0;
	private static int listIndex = 0;
	private static CharacterItem[] items;
	public Characters character;
	public HUD healths = new HUD(0,0,0);
	
	public void getInput() 
	{
		
	}
	
	public void update() 
	{	
		//if(listIndex == 0)
		{
			text.clear();
			listIndex=1;
			count3=0;
			items = new CharacterItem[Inventory.numOfItems];
			for(CharacterItem item: Inventory.items)
			{
				if(count3<items.length)
				{
					items[count3] = item;
					count3++;
					//System.out.println(item.getName());
				}
				else
				{
					count3 = 0;
					count2 = 140;
					topindex = 0;
					break;
				}	
			}
			/*if(items.length > 5)
			{
				topindex = 0;
				bottomindex = 5;
			}*/
		}
		
		text.clear();
		text.write("Inventory", 100, 155, font);
		text.write("Exit", 150, 90, font);
		if(KeyHandler.keyClick(Key.DOWN))
		{	
			topindex++;
			bottomindex++;
			
			if(bottomindex>items.length)
			{
				if(bottomindex == 0)
				{
					index = 5;
				}
				else
				{
					index++;
					topindex = items.length - 5;
					bottomindex = items.length;
				}
			}
			
		}
		if(KeyHandler.keyClick(Key.UP))
		{
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
		}
		
		if(KeyHandler.keyClick(Key.U))
			GameHandler.switchState(States.GAME);
		
		//NOTE: remove the print statements.
		if(KeyHandler.keyClick(Key.E))
		{
			System.out.println("The conditional works");

			if(index == 5)
			{
				if(ShopState.sell)
				{
					ShopState.sell = false;
					System.out.println("The boolean works");
				}
				else
				{
					index = 0;
					listIndex = 0;
					GameHandler.switchState(States.GAME);
				}
				System.out.println("The index works");
			}
			else
			{
				if(ShopState.sell)
				{
					character = Map.currentPlayer();
				    healths = Map.getPlayer().getCharacterHUD(character);
				    healths.giveMoney(Inventory.getInventoryItem(index).getResalePrice());
					Inventory.removeItem(Inventory.getInventoryItem(index));
					System.out.println("Removing works");
				}
			}
			/*if(index == 0) 
				if(!item1.equipped)
					item1.equip();
				else
					item1.unequip();
			else if(index == 1) 
				if(!item2.equipped)
					item2.equip();
				else
					item2.unequip();
			else if(index == 2) 
				if(!item3.equipped)
					item3.equip();
				else
					item3.unequip();*/
			
			/*if(index == 0)
				items[0].useItem();
			else if(index == 1)
				items[1].useItem();
			else if(index == 2)
				items[2].useItem();*/
			
		}
		
		if(items.length<=5)
		{
			topindex = 0;
			bottomindex = items.length;
			count2 = 140;
		}
		
		for(int i=topindex; i<bottomindex; i++)
		{
			if(count2 == 90)
			{
				count2 = 140;
			}
			//It prints it out in the correct order however it does not display in the correct order?!
			//System.out.println(items[i].getName());
			text.write(items[i].getName() + "   " + items[i].getQuantity() + "     " + items[i].getResalePrice(), count, count2, font);
			count2-=10;
		}	
	}

	
	public void render() 
	{
		Draw.rect(10, 75, 240, 100, 0, 0, 220, 59, 6);
		
		if(index > 5)index = 5;
		
		if(items.length < 5 && index == items.length)
			index = 5;
		//this needs to be modified to be an else if, but what's the condition?
		
		
		if(index < 0)index = 0;
		
		if(bottomindex == items.length || items.length - 5 <= bottomindex)
		{
			if(bottomindex == 0)
				index = 5;
			if(index == 0)Draw.rect(27, 142, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 1)Draw.rect(27, 132, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 2)Draw.rect(27, 122, 2, 2, 48, 3, 50, 5,5);
			else if(index == 3)Draw.rect(27, 112, 2, 2, 48, 3, 50, 5, 5);
			else if(index == 4)Draw.rect(27, 102, 2, 2, 48, 3, 50, 5,5);
			else if(index == 5)Draw.rect(147, 92, 2, 2, 48, 3, 50, 5,5);
		}
		else Draw.rect(27, 142, 2, 2, 48, 3, 50, 5, 5);
		
		text.render();

	}

}
