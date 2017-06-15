package komorebi.projsoul.items;

//Represents a key item that unlocks, it can be a key or a token of sorts (Cannot be sold by the character)
//Doesn't have to be something that unlocks a door, it could open a new area, or a tournament, etc..
public class KeyItem extends Item
{
	//Indicates what the key item unlocks
	private String unlocks;
	
	public KeyItem(String name, String unlock) 
	{
		super(name);
		unlocks = unlock;
	}

}
