package komorebi.projsoul.items;

//Represents a Ring (which all have a special effect :D)
public class Ring extends Armor 
{
	String effect;

	public Ring(String name, int salePrice, int quantity, int defense, int durability, String description,String specialEffect) 
	{
		super(name, salePrice, quantity, defense, durability, description);
		effect = specialEffect;
		
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
