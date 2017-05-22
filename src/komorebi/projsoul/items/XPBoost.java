package komorebi.projsoul.items;

import komorebi.projsoul.engine.Inventory;
import komorebi.projsoul.entities.BaseXPObject;
import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.states.Game;

public class XPBoost extends Potion 
{
	Characters character;
	
	public XPBoost(String name, int salePrice, int quantity, String effectDescription, int effect)
	{
		super(name, salePrice, quantity, "Gives " + effect + " points of xp", effect);
	}

	
	public void useItem() 
	{
		Game.getMap().addXPObject(new BaseXPObject(Map.getPlayer().getX()+25, Map.getPlayer().getY(), effect));
		Game.getMap().addXPObject(new BaseXPObject(Map.getPlayer().getX()+30, Map.getPlayer().getY(), effect));
		Game.getMap().addXPObject(new BaseXPObject(Map.getPlayer().getX()+35, Map.getPlayer().getY(), effect));
		
		Inventory.removeItem(this);
		
	}

}
