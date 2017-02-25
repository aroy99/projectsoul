package komorebi.projsoul.editor.history;

import komorebi.projsoul.editor.Editor;
import komorebi.projsoul.editor.modes.MoveMode;
import komorebi.projsoul.engine.Draw;

public class MovementPermissionRevision extends Revision {
  
  private PermissionArrangement[] prePermissions; //what the tiles 
                                                  //were before
  private PermissionArrangement[] postPermissions; //what they are after
  
  private MoveMode moveMode;
  
  public MovementPermissionRevision(PermissionArrangement[] pre,
      PermissionArrangement[] post) {
    super();
    
    prePermissions = pre;
    postPermissions = post;
    
    moveMode = Editor.getMap().getMoveMode();
    description = "Movement permission changed";
  }

  @Override
  public void undo() {
    for (PermissionArrangement arrangement: prePermissions)
    {
      moveMode.applyPermissions(arrangement);
    }
    
  }

  @Override
  public void redo() {
    for (PermissionArrangement arrangement: postPermissions)
    {
      moveMode.applyPermissions(arrangement);
    }
    
  }

  @Override
  public void render() {
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, clickableArea.x, 
        clickableArea.y, 16, 16, 16, 32, 32, 48, 2);    
    
  }

}
