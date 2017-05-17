package komorebi.projsoul.items;

import komorebi.projsoul.engine.Inventory;
import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.map.Map;

public class MaxMagicBoost extends Potion
{
	Characters character;

	public MaxMagicBoost(String name, int salePrice, int quantity, String effectDescription, int effect) 
	{
		super(name, salePrice, quantity, "Boosts max magic permenantly by " + effect + " points", effect);
	}

	
	public void useItem() 
	{
		character = Map.currentPlayer();
	    Map.getPlayer().getCharacterHUD(character).addToMaxMagic(effect);
	    Inventory.removeItem(this);
	}

}
