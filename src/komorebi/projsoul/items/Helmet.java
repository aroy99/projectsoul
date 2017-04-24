package komorebi.projsoul.items;

import komorebi.projsoul.entities.Bruno;
import komorebi.projsoul.entities.Caspian;
import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.entities.Flannery;
import komorebi.projsoul.entities.Sierra;
import komorebi.projsoul.map.Map;

//Represents a Helmet piece of armor
public class Helmet extends Armor 
{
	Characters character;
	
	public Helmet(String name, int salePrice, int quantity, int defense, int durability, String description)
	{
		super(name, salePrice, quantity, defense, durability, description);
	}

	//Might need different code for these methods (unsure as of right now)
	public void equip() 
	{
		super.equip();
				
	}

	public void unequip() 
	{
		super.unequip();
		
	}
}
