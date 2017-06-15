package komorebi.projsoul.items;

import komorebi.projsoul.entities.player.Characters;

//Represents a Helmet piece of armor
public class Helmet extends Armor 
{
  Characters character;

  public Helmet(String name, int salePrice, int quantity, int defense, int durability, String description)
  {
    super(name, salePrice, quantity, defense, durability, description);
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
