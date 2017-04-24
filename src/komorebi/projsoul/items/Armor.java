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
	public int durability;
	public boolean equipped;
	Characters character;
	Characters equippedCharacter;
	
	public Armor(String name, int salePrice, int quantity, int defense, int durability, String description) 
	{
		super(name, salePrice, quantity, description);
		this.defense = defense;
		this.durability = durability;
	}
	
	public void equip() 
	{
		character = Map.currentPlayer();
		//Armor cannot be unequiped from a character it isnt equipped to
		//Needs to be conditionals for that and removing armor from inventory when it is equipped
		switch (character)
		{
			case CASPIAN:
				Caspian.addDefense(defense);
				System.out.println(Caspian.getDefense(character));
				break;
			case FLANNERY:
				Flannery.addDefense(defense);
				System.out.println(Flannery.getDefense(character));
				break;
			case SIERRA:
				Sierra.addDefense(defense);
				System.out.println(Sierra.getDefense(character));
				break;
			case BRUNO:
				Bruno.addDefense(defense);
				System.out.println(Bruno.getDefense(character));
				break;
		}	
		equipped = true;
	}

	public void unequip() 
	{
		character = Map.currentPlayer();
		
		switch (character)
		{
			case CASPIAN:
				Caspian.subDefense(defense);
				System.out.println(Caspian.getDefense(character));
				break;
			case FLANNERY:
				Flannery.subDefense(defense);
				System.out.println(Flannery.getDefense(character));
				break;
			case SIERRA:
				Sierra.subDefense(defense);
				System.out.println(Sierra.getDefense(character));
				break;
			case BRUNO:
				Bruno.subDefense(defense);
				System.out.println(Bruno.getDefense(character));
				break;
		}
		equipped = false;
	}

}
