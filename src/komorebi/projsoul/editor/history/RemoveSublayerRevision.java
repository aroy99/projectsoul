package komorebi.projsoul.editor.history;

import java.util.ArrayList;

import komorebi.projsoul.editor.Sublayer;
import komorebi.projsoul.engine.Draw;

public class RemoveSublayerRevision extends Revision {

  private ArrayList<Sublayer> removedFrom;
  private Sublayer removed;
  
  public RemoveSublayerRevision(Sublayer removed, ArrayList<Sublayer> removedFrom)
  {
    this.removed = removed;
    this.removedFrom = removedFrom;
    
    description = "Removed " + removed.getTextField().getText();
  }
  
  @Override
  public void undo() {
    removedFrom.add(removed);
  }

  @Override
  public void redo() {
    removedFrom.remove(removed);
  }

  @Override
  public void render() {
    Draw.rect(clickableArea.x, clickableArea.y, 
        16, 16, 11, 104, 27, 120, 2);
  }
  
  
}
