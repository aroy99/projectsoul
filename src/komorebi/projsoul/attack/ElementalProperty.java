package komorebi.projsoul.attack;

import java.util.ArrayList;

import komorebi.projsoul.entities.Entity;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.entities.enemy.Chaser;
import komorebi.projsoul.entities.enemy.Dummy;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.entities.player.Player;


public enum ElementalProperty {
	
	FIRE, WATER, WIND, EARTH;
	
	
	public static ElementalProperty toEnum(String s){
	    switch (s){
	      case "FIRE":
	        return ElementalProperty.FIRE;

	      case "WATER":
	        return ElementalProperty.WATER;
	        
	      case "WIND":
	    	  return ElementalProperty.WIND;
	    	  
	      case "EARTH":
	    	  return ElementalProperty.EARTH;

	      default:
	        return null;
	    			}
	  }
	  
	  
	  public String toString(){
	    switch(this){
	      case FIRE:  return "FIRE";
	      case WATER: return "WATER";
	      case WIND:  return "WIND";
	      case EARTH: return "EARTH";
	      default:    return "bleh";
	    			}
	  }

	  /**
	   * @return all of the strings in this enum
	   */
	  public static ArrayList<String> allStrings(){
	    ArrayList<String> a = new ArrayList<String>();
	    for(ElementalProperty type : values()){
	      a.add(type.toString());}
	    	return a;
	  	}
	  
	  //Takes in Enemy hurting the Player's type and You(victim)'s type
	  //Takes in Your Attack's type and Enemy's type
	  public double findEffectiveness(ElementalProperty Attacker, ElementalProperty victim){
			switch(victim){
				case FIRE:
					switch(Attacker){
						case FIRE: case WIND:
							return 1;
						case WATER:
							return 1.5;
						case EARTH:
							return 0.7;
									}
				case WATER:
					switch(Attacker){
						case WATER: case EARTH:
							return 1;
						case WIND:
							return 1.5;
						case FIRE:
							return 0.7;
									}
				case WIND:
					switch(Attacker){
						case WIND: case FIRE:
							return 1;
						case EARTH:
							return 1.5;
						case WATER:
							return 0.7;
									}
				case EARTH:
					switch(Attacker){
						case EARTH: case WATER:
							return 1;
						case FIRE:
							return 1.5;
						case WIND:
							return 0.7;
									}
			default:
				return 1;
			}
	  }
	  
	  public double calcStab(ElementalProperty curry, ElementalProperty attack){
		  switch(curry){
		  	case WATER:
		  		switch (attack){
		  			case WATER:
		  				return 1.3;
		  			default:
		  				return 1;
		  						}
		  	case FIRE:
		  		switch (attack){
		  			case FIRE:
		  				return 1.3;
		  			default:
		  				return 1;
		  						}
		  	case WIND:
		  		switch (attack){
		  			case WIND:
		  				return 1.3;
		  			default:
		  				return 1;
		  						}
		  	case EARTH:
		  		switch(attack){
		  			case EARTH:
		  				return 1.3;
		  			default:
		  				return 1;
		  						}
		  	default:
		  		return 1;
		  }
		  
	  }

	
}
