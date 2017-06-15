package komorebi.projsoul.items;

//Represents any potion that can have any effect (heal, heal mana, do damage)
public abstract class Potion extends Consumable
{
	public String effectDescription;
	//# of points the potion heals/upgrades/boosts
	public int effect;
	
	public Potion(String name, int salePrice, int quantity, String effectDescription, int effect) 
	{
		super(name, salePrice, quantity, effectDescription);
		this.effectDescription = effectDescription;
		this.effect = effect;
	}

}
