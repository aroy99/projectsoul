package komorebi.projsoul.editor.modes.event;

import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.editor.modes.EventMode.EventTypes;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.map.EditorMap;

public class NPCEvent extends Event
{ 
  public NPCEvent(NPCType sprite, String name, String walkScript, 
      String talkScript)
  {
    this.sprite = sprite;
    this.name = name;
    this.walkScript = walkScript;
    this.talkScript = talkScript;
    
    type = EventTypes.NPC;
  }
  
  public Event duplicate()
  {
    NPCEvent duplicate = new NPCEvent(sprite, name, walkScript, talkScript);
    duplicate.setTileLocation(tx, ty);
    
    return duplicate;
  }
  
  @Override
  public void applyAllAttributesOf(Event e) throws MismatchedClassException {
    
    if (!(e instanceof NPCEvent))
    {
      throw new MismatchedClassException("Attempted to apply the attributes of "
          + e.getClass() + " to " + this.getClass());
    }
    
    this.sprite = ((NPCEvent) e).getSpriteType();
    this.name = ((NPCEvent) e).getName();
    this.walkScript = ((NPCEvent) e).getWalkScript();
    this.talkScript = ((NPCEvent) e).getTalkScript();
    
    setTileLocation(e.getTileX(), e.getTileY());
    
  }
  
  String walkScript, talkScript, name;
  public void setWalkScript(String walk)
  {
    walkScript = walk;
  }
  
  public NPCType getSpriteType()
  {
    return sprite;
  }
  
  public void setSpriteType(NPCType sprite)
  {
    this.sprite = sprite;
  }
  
  public void setTalkScript(String talk)
  {
    talkScript = talk;
  }
  
  public String getWalkScript()
  {
    return walkScript;
  }
  
  public String getTalkScript()
  {
    return talkScript;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getName()
  {
    return name;
  }
  
  NPCType sprite;
  public void setSprite(NPCType sprite)
  {
    this.sprite = sprite;
  }
  
  @Override
  public void renderE() {
    switch (sprite)
    {
      case NESS:
        Draw.rectZoom(x, y, 16, 24, 0, 0, 16, 24, 4, 
            Editor.zoom(), EditorMap.getX(), EditorMap.getY()); 
        break;
      case POKEMON:
        Draw.rectZoom(x, y, 16, 24, 1, 0, 17, 24, 3, 
            Editor.zoom(), EditorMap.getX(), EditorMap.getY());         break;
      default:
        break;
      
    }
    
  }  
  
  public String toString()
  {
    return "npc " + name + " " + x + " " + y + " " + type.toString() + " " +
          walkScript + " " + talkScript;
  }
}