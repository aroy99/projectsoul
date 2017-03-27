package komorebi.projsoul.script;

import java.util.NoSuchElementException;

import komorebi.projsoul.entities.Face;

public enum Alignment {

  LEFT(Face.LEFT, "left"), RIGHT(Face.RIGHT, "right"), 
  UP(Face.UP, "up"), DOWN(Face.DOWN, "down"), 
  TO_NPC(null, "");
  
  private Face direction;
  private String asString;
  
  private Alignment(Face direction, String asString)
  {
    this.direction = direction;
    this.asString = asString;
  }
  
  public boolean alignsToNPC()
  {
    return this == TO_NPC;
  }
  
  public Face getDirection()
  {
    return direction;
  }
  
  public String toString()
  {
    return asString;
  }
 
  
  public static Alignment getAlignment(String match) throws
    NoSuchElementException
  {
    for (Alignment alignment: values())
      if (alignment.toString().equals(match))
        return alignment;
    
    throw new NoSuchElementException();
  }
}
