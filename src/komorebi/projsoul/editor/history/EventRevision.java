package komorebi.projsoul.editor.history;

import komorebi.projsoul.editor.modes.event.Event;
import komorebi.projsoul.editor.modes.event.MismatchedClassException;
import komorebi.projsoul.engine.Draw;

public class EventRevision extends Revision {

  private Event revised; //contains object reference to the revised event
  private Event pre, post;
  
  /**
   * Object representing a single edit in Event Mode
   * @param revised The Event object being revised
   * @param pre The states of the Event object prior to the edit
   * @param post The states of the Event object after the edit
   * 
   * 
   */
  public EventRevision(Event revised, Event pre, Event post)
  {
    super();
    
    this.revised = revised;
    
    this.pre = pre.duplicate();
    this.post = post.duplicate();
    
    description = "Changed attributes of an event";
    
  }
  
  @Override
  public void undo() {
    try {
      revised.applyAllAttributesOf(pre);
    } catch (MismatchedClassException e) {
      e.printStackTrace();
    }
    
  }
  @Override
  public void redo() {
    try {
      revised.applyAllAttributesOf(post);
    } catch (MismatchedClassException e) {
      e.printStackTrace();
    }
    
  }
  @Override
  public void render() {
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, clickableArea.x, 
        clickableArea.y, 16, 16, 32, 32, 48, 48, 2);    
  } 
  
  
}
