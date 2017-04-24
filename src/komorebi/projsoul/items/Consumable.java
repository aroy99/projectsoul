package komorebi.projsoul.items;

//Represents an item that can be used, and is a one-time-use
public abstract class Consumable extends CharacterItem
{
	public Consumable(String name, int salePrice, int quantity, String description) 
	{
		super(name, salePrice, quantity, description);
	}
	
	public abstract void useItem();

}
