package komorebi.projsoul.items;

public class Bomb extends ThrowableWeapon 
{
	public int explosionRadius;

	public Bomb(String name, int salePrice, int quantity, int damage, int range, int radius, String description) 
	{
		super(name, salePrice, quantity, damage, range, description);
		explosionRadius = radius;
	}

	
	public void useItem() 
	{
		

	}

}
