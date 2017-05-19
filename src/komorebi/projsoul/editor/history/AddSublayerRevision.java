package komorebi.projsoul.editor.history;

import java.util.ArrayList;

import komorebi.projsoul.editor.Sublayer;
import komorebi.projsoul.engine.Draw;

public class AddSublayerRevision extends Revision {

  private Sublayer added;
  private ArrayList<Sublayer> addedTo;
  
  public AddSublayerRevision(Sublayer added, ArrayList<Sublayer> addedTo)
  {
    this.added = added;
    this.addedTo = addedTo;
    
    description = "Added " + added.getTextField().getText();
  }
  
  @Override
  public void undo() {
    addedTo.remove(added);
    
  }

  @Override
  public void redo() {
    addedTo.add(added);
  }

  @Override
  public void render() {
    Draw.rect(clickableArea.x + 2, clickableArea.y + 2, 12, 12, 
        0, 92, 12, 104, 2);
    
  }
}
