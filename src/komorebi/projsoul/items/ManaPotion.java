package komorebi.projsoul.items;

import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.map.Map;

public class ManaPotion extends Potion 
{
	Characters character;

	public ManaPotion(String name, int salePrice, int quantity, int effect) 
	{
		super(name, salePrice, quantity, "Restores magic by " + effect + " points", effect);
	}

	@Override
	public void useItem() 
	{
		character = Map.currentPlayer();
		int dif = Map.getPlayer().getCharacterHUD(character).getMaxMagic() - Map.getPlayer().getCharacterHUD(character).getMagic();
		if(dif > 0 && dif < effect)
		{
			Map.getPlayer().getCharacterHUD(character).changeMagicBy(dif);
			quantity--;
		}
		else if(Map.getPlayer().getCharacterHUD(character).fullMagic())
		{
			System.out.println("Your magic is full!");
		}
		else
		{
			Map.getPlayer().getCharacterHUD(character).changeMagicBy(effect);
			quantity--;
		}

	}

}
