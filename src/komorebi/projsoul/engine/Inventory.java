package komorebi.projsoul.engine;

import java.util.ArrayList;
import komorebi.projsoul.items.CharacterItem;

public abstract class Inventory 
{
	//ArrayList or Array????????????????????
	//public static CharacterItem[] items = new CharacterItem[10];
	public static ArrayList<CharacterItem> items = new ArrayList<CharacterItem>();
	public static int numOfItems;
	private static int checked = 0;
	
	public static void addItem(CharacterItem item)
	{
		for(CharacterItem myItem: items)
		{
			if(myItem.getName() == item.getName())
			{
				myItem.increaseQuantity(item.getQuantity());
				checked++;
				break;
			}
			else
			{
				checked = 0;
			}
				
		}
		
		if(checked == 0)
		{
			items.add(item);
			checked++;
		}
		numOfItems = items.size();
		
		/*if(items.size()-1 == size)
		{
			System.out.println("Sorry your inventory is full");
		}*/
	}
	
	public void printInventoryContents()
	{
		for(CharacterItem myItem: items)
		{
			System.out.println(myItem.getName());
		}
	}

}
