package komorebi.projsoul.gameplay;

import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.gameplay.Item.Items;



public class Equipment {

	
	
	public enum Equipables
	{
		
		BOOTS("timbs", "These are some nice Timbs."),
		HELMET("helmet", "a ugly helmet"),
		CHESTPLATE("chestplate", "a nice rack"),
		BALLOON("balloon","A big red balloon"),
		SWORD("sword", "a sharp sword"),
		WIG("wig", "a blonde wig"),
		NONE("none", "nothing");
		 
	    private String printString;
	    private String idString;
	    
	    private Equipables(String idString, String printString)
	    {
	      this.printString = printString;
	      this.idString = idString;
	    }
	    
	    public String getPrintString()
	    {
	      return printString;
	    }
	    
	    public String getIDString()
	    {
	      return idString;
	    }
	    
		 /**
	     * Returns the equipment with the specified name
	     * @param s The name of the equipment
	     * @return Gives corresponding Equipment item, or NONE if it doesn't exist
	     */
	    public static Equipables getEquipables(String s)
	    {
	      for (Equipables equipment: Equipables.values())
	      {
	        if (equipment.getIDString().equals(s)) 
	        {
	          return equipment;
	        }
	      }
	      return NONE;
	    }
	     
		
		/**
	     * @return A short Equipment Description
	     */
	    public String getEquipmentDescription() {
	      switch (this)
	      {
	        case BOOTS:
	          return "Stomp on stuff n\' shit\n+1 Defense";
	        case HELMET:
	          return "That thing you think in\n+2 Defense";
	        case CHESTPLATE:
	          return "Protects the good stuff\n+10 Health";
	        case BALLOON:
		      return "Nice and floaty balloon\n+10 Mana";
	        case SWORD:
	          return "Provides marriage advice\n+3 Attack";
	        case WIG:
	          return "Luscious glowing locks\nDoes nothing";
	        default:
	          return "";
		
	      }
	    }
	}
	private Equipables equipment;
	  
	  public Equipment(Equipables type)
	  {
	    equipment = type;
	  }
	 public Equipables type()
	  {
	    return equipment;
	  }

}
