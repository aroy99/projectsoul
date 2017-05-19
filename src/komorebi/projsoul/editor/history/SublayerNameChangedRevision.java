package komorebi.projsoul.editor.history;

import komorebi.projsoul.editor.Sublayer;
import komorebi.projsoul.engine.Draw;

public class SublayerNameChangedRevision extends Revision {

  private Sublayer sub;
  private String preName, postName;
  
  public SublayerNameChangedRevision(String preName,
      String postName, Sublayer sub)
  {
    this.sub = sub;
    this.preName = preName;
    this.postName = postName;
    
    description = "Renamed " + preName + " to " + postName;
  }
  
  @Override
  public void undo() {
    sub.getTextField().setText(preName);
    
  }

  @Override
  public void redo() {
    sub.getTextField().setText(postName);
    
  }

  @Override
  public void render() {
    Draw.rect(clickableArea.x+2, clickableArea.y, 
        12, 16, 0, 104, 12, 120, 2);
  }

}
