package komorebi.projsoul.editor.history;

import komorebi.projsoul.editor.modes.event.Event;
import komorebi.projsoul.engine.Draw;

public class AddEventRevision extends Revision {

  Event revised;
  
  public AddEventRevision(Event revised)
  {
    super();
    this.revised = revised;
    description = "Added " + revised.getType().toString();
    
  }
  
  @Override
  public void undo() {
    revised.getType().removeEvent(revised);
    
  }

  @Override
  public void redo() {
    revised.getType().addEvent(revised.getClass(), revised);
    
  }

  @Override
  public void render() {
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, clickableArea.x, 
        clickableArea.y, 16, 16, 151, 99, 167, 115, 2);
    
  }

}
