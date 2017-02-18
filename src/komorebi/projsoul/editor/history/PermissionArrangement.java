package komorebi.projsoul.editor.history;

import komorebi.projsoul.editor.modes.MoveMode.Permission;

public class PermissionArrangement {

  int layer;
  int tx, ty;
  Permission[][] arrangement;
  
  public PermissionArrangement(Permission[][] arrangement,
      int tx, int ty, int layer)
  {
    this.arrangement = arrangement;
    this.tx = tx;
    this.ty = ty;
    this.layer = layer;
  }
  
  public int getLayer()
  {
    return layer;
  }
  
  public int getTileX()
  {
    return tx;
  }
  
  public int getTileY()
  {
    return ty;
  }
  
  public Permission getPermissionAt(int tx, int ty)
  {
    return arrangement[ty][tx];
  }
  
  public int getHeight()
  {
    return arrangement.length;
  }
  
  public int getWidth()
  {
    return arrangement[0].length;
  }
}
