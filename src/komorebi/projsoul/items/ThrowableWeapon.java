package komorebi.projsoul.items;

//Represents any throwable weapon that inflicts damage
public abstract class ThrowableWeapon extends Consumable 
{
	public int damage;
	public int range;
	public ThrowableWeapon(String name, int salePrice, int quantity, int damage, int range, String description) 
	{
		super(name, salePrice, quantity, description);
		this.damage = damage;
		this.range = range;
				
	}

}
