package komorebi.projsoul.items;

import komorebi.projsoul.entities.Bruno;
import komorebi.projsoul.entities.Caspian;
import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.entities.Flannery;
import komorebi.projsoul.entities.Sierra;
import komorebi.projsoul.map.Map;

//Represents an armor item in the game, that can be equipped
public abstract class Armor extends CharacterItem 
{
	public int defense;
	public double maxDurability;
	public double durability;
	public boolean equipped;
	public boolean broken;
	Characters equippedCharacter;
	
	public Armor(String name, int salePrice, int quantity, int defense, double durability, String description) 
	{
		super(name, salePrice, quantity, description);
		this.defense = defense;
		maxDurability = this.durability = durability;
		equipped = false;
		broken = false;
	}
	
	public void equip() 
	{
		if(!broken)
		{
			equippedCharacter = Map.currentPlayer();
			switch (equippedCharacter)
			{
			case CASPIAN:
				Caspian.addDefense(defense);
				System.out.println("Caspian" + Caspian.getDefense(equippedCharacter));
				break;
			case FLANNERY:
				Flannery.addDefense(defense);
				System.out.println("Flannery" + Flannery.getDefense(equippedCharacter));
				break;
			case SIERRA:
				Sierra.addDefense(defense);
				System.out.println("Sierra" + Sierra.getDefense(equippedCharacter));
				break;
			case BRUNO:
				Bruno.addDefense(defense);
				System.out.println("Bruno" + Bruno.getDefense(equippedCharacter));
				break;
			}	
			equipped = true;
		}
	}

	public void unequip() 
	{
		switch (equippedCharacter)
		{
			case CASPIAN:
				Caspian.subDefense(defense);
				System.out.println("Caspian" + Caspian.getDefense(equippedCharacter));
				break;
			case FLANNERY:
				Flannery.subDefense(defense);
				System.out.println("Flannery" + Flannery.getDefense(equippedCharacter));
				break;
			case SIERRA:
				Sierra.subDefense(defense);
				System.out.println("Sierra" + Sierra.getDefense(equippedCharacter));
				break;
			case BRUNO:
				Bruno.subDefense(defense);
				System.out.println("Bruno" + Bruno.getDefense(equippedCharacter));
				break;
		}
		equipped = false;
		equippedCharacter = null;
	}
	public void damageArmor(int enemyAttack)
	{
		if(enemyAttack >= 10)
		{
			durability-= (double)1/4;
		}
		if(durability == 0)
		{
			broken = true;
			super.resalePrice = super.salePrice/8;
			unequip();
			defense = 0;
		}
		System.out.println(durability + "/" + maxDurability);
	}
	
	public Characters getEquippedCharacter()
	{
		return equippedCharacter;
	}
	
	public boolean checkEquipped()
	{
		return equipped;
	}
	
	public boolean checkBroken()
	{
		return broken;
	}
	
	public double getDurability()
	{
		return durability;
	}
	
	public double getMaxDurability()
	{
		return maxDurability;
	}
}
