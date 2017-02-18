package komorebi.projsoul.editor.modes.event;

import komorebi.projsoul.editor.modes.EventMode.EventTypes;
import komorebi.projsoul.map.EditorMap;

public abstract class Event {
  public abstract void renderE();
  public abstract Event duplicate();
  public abstract void applyAllAttributesOf(Event e) throws MismatchedClassException;
  
  protected EventTypes type;
  public EventTypes getType()
  {
    return type;
  }
  
  protected float x, y;
  public float getX()
  {
    return x;
  }
  
  public float getY()
  {
    return y;
  }
  
  protected int tx, ty;
  public int getTileX()
  {
    return tx;
  }
  
  public int getTileY()
  {
    return ty;
  }
  
  public void setPixLocation(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  
  
  public void setTileLocation(int tx, int ty)
  {
    this.x=tx*16+EditorMap.getX();
    this.y=ty*16+EditorMap.getY();
    this.tx = tx;
    this.ty = ty;
  }
}