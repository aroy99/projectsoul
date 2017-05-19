package komorebi.projsoul.editor.modes.event;

import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.editor.modes.EventMode.EventTypes;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.map.EditorMap;

public class AreaScriptEvent extends Event
{
  String script, myNpc;
  boolean repeat;
  
  public AreaScriptEvent(String script, boolean repeat, String myNpc)
  {
    this.script = script;
    this.repeat = repeat;
    this.myNpc = myNpc;
    
    this.type = EventTypes.SCRIPT;

  }
  
  public Event duplicate()
  {
    AreaScriptEvent duplicate = new AreaScriptEvent(
        script, repeat, myNpc);
    duplicate.setTileLocation(tx, ty);
    
    return duplicate;
  }
  
  @Override
  public void applyAllAttributesOf(Event e) throws MismatchedClassException {
    
    if (!(e instanceof AreaScriptEvent))
    {
      throw new MismatchedClassException("Attempted to apply the attributes of "
          + e.getClass() + " to " + this.getClass());
    }
    
    this.script = ((AreaScriptEvent) e).getScript();
    this.repeat = ((AreaScriptEvent) e).isRepeatable();
    this.myNpc = ((AreaScriptEvent) e).getNPC();
    
    setTileLocation(e.getTileX(), e.getTileY());
    
  }

  @Override
  public void renderE() {
    Draw.rectZoom(x, y, 16, 16, 32, 0, 48, 16, 2, 
        Editor.zoom(), EditorMap.getX(), EditorMap.getY());
    
  }
  
  public String toString()
  {
    if (myNpc != null && !myNpc.isEmpty())
      return "script " + script + " " + x + " " + y + 
          " " + repeat + " " + myNpc;
    
    return "script " + script + " " + x + " " + y + 
        " " + repeat;
  }
  
  public String getScript()
  {
    return script;
  }
  
  public void setScript(String script)
  {
    this.script = script;
  }
  
  public void setNPC(String npc)
  {
    myNpc = npc;
  }
  
  public boolean isRepeatable()
  {
    return repeat;
  }
  
  public void setIsRepeatable(boolean repeat)
  {
    this.repeat = repeat;
  }
  
  public String getNPC()
  {
    return myNpc;
  }

 
  
}