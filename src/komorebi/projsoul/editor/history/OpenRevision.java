package komorebi.projsoul.editor.history;

import komorebi.projsoul.engine.Draw;

public class OpenRevision extends Revision {

  public OpenRevision(String path)
  {    
    super();
    
    description = "Opened " + path;
  }
  
  @Override
  public void undo() {}

  @Override
  public void redo() {}

  @Override
  public void render() {
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, clickableArea.x, 
        clickableArea.y, 16, 16, 16, 48, 32, 64, 2);
  }

}
