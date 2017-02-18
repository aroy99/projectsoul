package komorebi.projsoul.editor.modes.event;

import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.editor.modes.EventMode.EventTypes;
import komorebi.projsoul.engine.Draw;
import komorebi.projsoul.entities.enemy.EnemyAI;
import komorebi.projsoul.entities.enemy.EnemyType;
import komorebi.projsoul.map.EditorMap;

public class EnemyEvent extends Event
{
  EnemyType sprite;
  EnemyAI ai;
  int radius;
  
  public EnemyEvent(EnemyType sprite, EnemyAI ai, int radius)
  {
    this.sprite = sprite;
    this.ai = ai;
    this.radius = radius;
    
    type = EventTypes.ENEMY;
  }
  
  public Event duplicate()
  {
    EnemyEvent duplicate = new EnemyEvent(
        sprite, ai, radius);
    duplicate.setTileLocation(tx, ty);
    
    return duplicate;
  }
  
  @Override
  public void applyAllAttributesOf(Event e) throws MismatchedClassException {
    
    if (!(e instanceof EnemyEvent))
    {
      throw new MismatchedClassException("Attempted to apply the attributes of "
          + e.getClass() + " to " + this.getClass());
    }
    
    this.sprite = ((EnemyEvent) e).getSpriteType();
    this.ai = ((EnemyEvent) e).getAIType();
    this.radius = ((EnemyEvent) e).getRadius();
    
    setTileLocation(e.getTileX(), e.getTileY());
    
  }

  @Override
  public void renderE() {
    switch (sprite)
    {
      case SATURN:
        Draw.rectZoom(x, y, 16, 21, 0, 0, 16, 21, 11, Editor.zoom(),
            EditorMap.getX(), EditorMap.getY());
        break;
      default:
        break;
      
    }
    
  }
  
  public void setSpriteType(EnemyType sprite)
  {
    this.sprite = sprite;
  }
  
  public EnemyType getSpriteType()
  {
    return sprite;
  }
  
  public void setAIType(EnemyAI ai)
  {
    this.ai = ai;
  }
  
  public EnemyAI getAIType()
  {
    return ai;
  }
  
  public int getRadius()
  {
    return radius;
  }
  
  public void setRadius(int radius)
  {
    this.radius = radius;
  }
  
  public String toString()
  {
    return "enemy " + x + " " + y + " " + sprite + " " + ai + " " +
        radius;
  }
}