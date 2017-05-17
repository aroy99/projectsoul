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
	private static boolean removed;
	
	public static void addItem(CharacterItem item)
	{
		for(CharacterItem myItem: items)
		{
			if(myItem.getName().equals(item.getName()))
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
		
	}
	
	//Specifically for the Sell part of the shop
	public static void removeItem(CharacterItem item)
	{
		for(int i=0; i<items.size(); i++)
		{
			if(item.getName().equalsIgnoreCase(items.get(i).getName()))
			{
				if(item.getQuantity()==1)
				{
					items.remove(item);
					numOfItems--;
					removed = true;
				}
				else
				{
					item.subQuantity();
					removed = false;
				}
			}
		}
	}
	
	public static Boolean checkRemoved()
	{
		return removed;
	}
	
	public static CharacterItem getInventoryItem(int index)
	{
		return items.get(index);
	}
	
	public void printInventoryContents()
	{
		for(CharacterItem myItem: items)
		{
			System.out.println(myItem.getName());
		}
	}

}
