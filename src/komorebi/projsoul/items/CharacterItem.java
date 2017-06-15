package komorebi.projsoul.items;

import komorebi.projsoul.engine.Inventory;

//Represents an item that is able to be purchased and sold by the character
public  abstract class CharacterItem extends Item
{
	public int salePrice;
	public int resalePrice;
	public int quantity;
	public String description;
	
	public CharacterItem(String name, int salePrice, int quantity, String description) 
	{
		super(name);
		this.salePrice = salePrice;
		this.description = description;
		this.quantity = quantity;
		if(salePrice % 2 != 0)
			resalePrice = (salePrice-1) / 2;
		else
			resalePrice = salePrice / 2;
	}
	
	public int getSalePrice()
	{
		return salePrice;
	}
	
	public int getResalePrice()
	{
		return resalePrice;
	}
	
	public int getQuantity()
	{
		return quantity;
	}
	
	public void increaseQuantity(int add)
	{
		quantity+=add;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void addDescription(String s)
	{
		description = s;
	}
	
	public void subQuantity()
	{
		quantity--;
	}
	
	public void addToInventory(CharacterItem item, int numSold)
	{
		item.increaseQuantity(numSold);
		Inventory.addItem(item);
	}

}
