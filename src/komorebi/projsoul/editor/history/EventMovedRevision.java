package komorebi.projsoul.editor.history;

import komorebi.projsoul.editor.modes.event.Event;
import komorebi.projsoul.engine.Draw;

public class EventMovedRevision extends Revision {

  private Event revised;
  private int preX, preY;
  private int postX, postY;
  
  public EventMovedRevision(Event revised, int preX, int preY, int postX,
      int postY)
  {
    super();
    
    this.revised = revised;
    
    this.preX = preX;
    this.preY = preY;
    
    this.postX = postX;
    this.postY = postY;
    
    description = "Moved an event";

  }
  
  @Override
  public void undo() {
    revised.setTileLocation(preX, preY);
  }

  @Override
  public void redo() {
    revised.setTileLocation(postX, postY);
  }

  @Override
  public void render() {
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, clickableArea.x, 
        clickableArea.y, 16, 16, 32, 32, 48, 48, 2);    
  }

  
}
