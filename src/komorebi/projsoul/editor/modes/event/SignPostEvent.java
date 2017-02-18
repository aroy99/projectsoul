package komorebi.projsoul.editor.modes.event;

import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.editor.modes.EventMode.EventTypes;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.map.EditorMap;

public class SignPostEvent extends Event
{
  private String message;
  
  public SignPostEvent(String message)
  {
    this.message = message;
    this.type = EventTypes.SIGN;
  }
  
  public Event duplicate()
  {
    SignPostEvent duplicate = new SignPostEvent(
        message);
    duplicate.setTileLocation(tx, ty);
    
    return duplicate;
  }
  
  @Override
  public void applyAllAttributesOf(Event e) throws MismatchedClassException {
    
    if (!(e instanceof SignPostEvent))
    {
      throw new MismatchedClassException("Attempted to apply the attributes of "
          + e.getClass() + " to " + this.getClass());
    }
    
    this.message = ((SignPostEvent) e).getText();
    
    setTileLocation(e.getTileX(), e.getTileY());
    
  }

  @Override
  public void renderE() {
    Draw.rectZoom(tx*16+EditorMap.getX(), ty*16+EditorMap.getY(), 
        16, 16, 96, 0, 112, 16, 2, Editor.zoom(), EditorMap.getX(),
        EditorMap.getY());
  }
  
  public String getText()
  {
    return message;
  }
  
  public void setText(String message)
  {
    this.message = message;
  }
  
  public String toString()
  {
    return "sign " + x + " " + y + " " + message;
  }
  
}