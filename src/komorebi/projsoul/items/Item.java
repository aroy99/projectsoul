/**
 * Item.java    Aug 3, 2016, 11:49:22 AM
 */
package komorebi.projsoul.items;

/**
 * 
 * @author Kevin Ehresman
 * @version 
 */
//Represents an item in the game
public abstract class Item 
{
	public String name;
	
	public Item(String name)
	{
		this.name = name;
	}  
	
	public String getName()
	{
		return name;
	}
	
}
