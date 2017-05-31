package komorebi.projsoul.editor.modes.event;

import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.editor.modes.event.EventMode.EventTypes;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.map.EditorMap;

public class WarpScriptEvent extends Event
{
  String warpTo;
  int warpX, warpY;
  
  public WarpScriptEvent(String warpTo, int warpX, int warpY)
  {
    this.warpTo = warpTo;
    this.warpX = warpX;
    this.warpY = warpY;
    
    this.type = EventTypes.WARP;
  }
  
  public Event duplicate()
  {
    WarpScriptEvent duplicate = new WarpScriptEvent(
        warpTo, warpX, warpY);
    duplicate.setTileLocation(tx, ty);
    
    return duplicate;
  }
  
  @Override
  public void applyAllAttributesOf(Event e) throws MismatchedClassException {
    
    if (!(e instanceof WarpScriptEvent))
    {
      throw new MismatchedClassException("Attempted to apply the attributes of "
          + e.getClass() + " to " + this.getClass());
    }
    
    this.warpTo = ((WarpScriptEvent) e).getMap();
    this.warpX = ((WarpScriptEvent) e).getWarpToX();
    this.warpY = ((WarpScriptEvent) e).getWarpToY();
    
    setTileLocation(e.getTileX(), e.getTileY());
    
  }
  
  @Override
  public void renderE() {
    Draw.rectZoom(x, y, 16, 16, 32, 0, 48, 16, 2, 
        Editor.zoom(), EditorMap.getX(), EditorMap.getY());
    
  }
  
  public void setMap(String map)
  {
    warpTo = map;
  }
  
  public void setWarpToX(int warpX)
  {
    this.warpX = warpX;
  }
  
  public void setWarpToY(int warpY)
  {
    this.warpY = warpY;
  }
  
  public String getMap()
  {
    return warpTo;
  }
  
  public int getWarpToX()
  {
    return warpX;
  }
  
  public int getWarpToY()
  {
    return warpY;
  }
  
  public String toString()
  {
    return "warp " + warpTo + " " + x + " " + y + " " + warpX + " " +
         warpY;
  }
}