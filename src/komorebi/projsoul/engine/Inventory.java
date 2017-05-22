package komorebi.projsoul.engine;

import java.util.ArrayList;

import komorebi.projsoul.items.Armor;
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
					if(item instanceof Armor)
					{
						Armor a = (Armor)item;
						if(a.equipped)a.unequip();
					}
				}
				else
				{
					item.subQuantity();
					removed = false;
				}
				System.out.println("1 " + item.getName() + "(s) were removed from your inventory");
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
