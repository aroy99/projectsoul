package komorebi.projsoul.items;

import komorebi.projsoul.engine.Inventory;
import komorebi.projsoul.entities.Bruno;
import komorebi.projsoul.entities.Caspian;
import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.entities.Flannery;
import komorebi.projsoul.entities.Player;
import komorebi.projsoul.entities.Sierra;
import komorebi.projsoul.map.Map;

public class XPBoost extends Potion 
{
	Characters character;
	
	public XPBoost(String name, int salePrice, int quantity, String effectDescription, int effect)
	{
		super(name, salePrice, quantity, "Gives " + effect + " points of xp", effect);
	}

	
	public void useItem() 
	{
		/*character = Map.currentPlayer();
		switch (character)
		{
		//Wants me to change the method to static
		case CASPIAN:
			Caspian.giveXP(5);
			Player.this.giveXP(5);
			Map.getPlayer().giveXP(5);
		case FLANNERY:
			
		case SIERRA:
			
		case BRUNO:
			
		}*/
		Inventory.removeItem(this);
		
	}

}
