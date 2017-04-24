package komorebi.projsoul.states;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.engine.GameHandler;
import komorebi.projsoul.engine.Inventory;
import komorebi.projsoul.engine.Key;
import komorebi.projsoul.engine.KeyHandler;
import komorebi.projsoul.items.CharacterItem;
import komorebi.projsoul.items.Potion;
import komorebi.projsoul.script.EarthboundFont;
import komorebi.projsoul.script.TextHandler;

public class InventoryState extends State 
{
	private TextHandler text = new TextHandler();
	EarthboundFont font = new EarthboundFont(1);
	@SuppressWarnings("unused")
	private int index = 0;
	private int topindex = 0;
	private int bottomindex = 5;
	private int count = 50;
	private int count2 = 140;
	private int check = 0;
	private static int count3 = 0;
	private static int listIndex = 0;
	private static CharacterItem[] items;
	
	public void getInput() 
	{
		
	}
	
	public void update() 
	{	
		
		if(listIndex == 0)
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
					System.out.println(item.getName());
				}
				else
				{
					count3 = 0;
					count2 = 140;
					topindex = 0;
					break;
				}	
			}
			if(items.length > 5)
			{
				topindex = 0;
				bottomindex = 5;
			}
		}
		
		text.clear();
		text.write("Inventory", 100, 155, font);
		if(KeyHandler.keyClick(Key.DOWN))
		{
			index++;
			topindex++;
			bottomindex++;
		
			if(bottomindex>items.length)
			{
				index = items.length-1;
				topindex = items.length - 5;
				bottomindex = items.length;
			}
		}
		if(KeyHandler.keyClick(Key.UP))
		{
			index--;
			topindex--;
			bottomindex--;
		
			if(topindex<0)
			{
				index = 0;
				topindex = 0;
				bottomindex = 5;
			}
		}
		
		if(KeyHandler.keyClick(Key.ENTER))
		{
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
		if(KeyHandler.keyClick(Key.I))
		{
			listIndex=0;
			GameHandler.switchState(States.GAME);
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
			text.write(items[i].getName() + "   " + items[i].getQuantity(), count, count2, font);
			count2-=10;
		}	
	}

	
	public void render() 
	{
		Draw.rect(10, 75, 240, 100, 0, 0, 220, 59, 6);
		
		/*while(index < items.length)
		{
			Draw.rect(count-3, count2+3, 2, 2, 48, 3, 50, 5, 5);
		}*/
		
		//index = 0;
		
		/*if(index == 0)Draw.rect(27, 142, 2, 2, 48, 3, 50, 5, 5);
		else if(index == 1)Draw.rect(27, 132, 2, 2, 48, 3, 50, 5, 5);
		else if(index == 2)Draw.rect(27, 122, 2, 2, 48, 3, 50, 5,5);*/
		
		/*if(index > 2)index = 0;
		else if(index < 0) index = 2;*/
		
		text.render();

	}

}
