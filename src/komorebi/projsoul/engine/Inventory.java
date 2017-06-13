package komorebi.projsoul.engine;

import java.util.ArrayList;

import komorebi.projsoul.items.Armor;
import komorebi.projsoul.items.CharacterItem;

public abstract class Inventory 
{
	public static ArrayList<CharacterItem> items = new ArrayList<CharacterItem>();
	public static int numOfItems;
	private static int checked = 0;
	private static boolean removed;
	
	public static void addItem(CharacterItem item)
	{
		if(numOfItems == 0)
		{
			checked = 0;
		}
		for(CharacterItem myItem: items)
		{
			if(myItem.getName().equals(item.getName()))
			{
				if(!(item instanceof Armor))
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
			else
			{
				checked = 0;
			}
				
		}
		
		if(checked == 0)
		{
			items.add(item);
			//System.out.println("Item added");
			numOfItems++;
			checked++;
		}
		
	}
	
	//For removing an item with an unknown index
	public static void removeItem(CharacterItem item)
	{
		for(int i=0; i<items.size(); i++)
		{
			if(item.getName().equalsIgnoreCase(items.get(i).getName()))
			{
				if(item.getQuantity()==1)
				{
					if(item instanceof Armor)
					{
						Armor a = (Armor)item;
						if(a.equipped)a.unequip();
					}
					items.remove(item);
					numOfItems--;
					removed = true;
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
	
	//For removing an item when you need to know the specific index in the Inventory
	public static void removeItem(CharacterItem item, int index)
	{
		if(item.getQuantity()==1)
		{
			if(item instanceof Armor)
			{
				Armor a = (Armor)item;
				if(a.equipped)a.unequip();
			}
			items.remove(index);
			numOfItems--;
			removed = true;
		}
		else
		{
			item.subQuantity();
			removed = false;
		}
		System.out.println("1 " + item.getName() + "(s) were removed from your inventory");
	}
	
	public static Boolean checkRemoved()
	{
		return removed;
	}
	
	public static CharacterItem getInventoryItem(int index)
	{
		return items.get(index);
	}

}
