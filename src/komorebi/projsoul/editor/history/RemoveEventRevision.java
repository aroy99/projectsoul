package komorebi.projsoul.editor.history;

import komorebi.projsoul.editor.modes.event.Event;
import komorebi.projsoul.engine.Draw;

public class RemoveEventRevision extends Revision {

 Event revised;
  
  public RemoveEventRevision(Event revised)
  {
    super();
    
    this.revised = revised;
    description = "Removed " + revised.getType().toString();
    
  }
  
  @Override
  public void undo() {
    revised.getType().addEvent(revised.getClass(), revised);
  }

  @Override
  public void redo() {
    revised.getType().removeEvent(revised);
  }

  @Override
  public void render() {
    Draw.rect(clickableArea.x, clickableArea.y, 16, 16, 173, 100, 189, 116, 2);
    
  }
}
