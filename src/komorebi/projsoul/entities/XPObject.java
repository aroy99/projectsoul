package komorebi.projsoul.entities;

import java.awt.Rectangle;

import komorebi.projsoul.map.Map;
import komorebi.projsoul.states.Game;

public class XPObject extends BaseXPObject {

  boolean[] hitBy;
  public XPObject(float x, float y, int xp, boolean[] hitBy) {
    super(x, y, xp);
    
    area = new Rectangle((int) x, (int) y, 8, 8);
    this.hitBy = hitBy; 
    
    this.xp = xp;
  }
  public void eat() {
	    destroyMe = true;
	    
	    int halfXP = (int) Math.ceil(xp / (double) 2);
	    
	    //Gives half the XP to the player who picked up the object
	    Map.getPlayer().giveXP(halfXP);
	    
	    int denom = 0;
	    
	    for (boolean b: hitBy)
	    {
	      if (b) denom++;
	    }
	    
	    int split = (int) Math.ceil(halfXP / (double) denom);
	    
	    //Splits the remaining half amongst all players who contributed
	    //to the kill
	    for (int i = 0; i < hitBy.length; i++)
	    {
	      if (hitBy[i])
	      {
	        Game.getMap().giveXP(Characters.getCharacter(i), split);  
	      }
	    }
	    
	  }

}
