package komorebi.projsoul.editor.history;

import komorebi.projsoul.editor.modes.MoveMode.Permission;

public class PermissionArrangement {

  int layer;
  int tx, ty;
  Permission[][] arrangement;
  
  public PermissionArrangement(Permission[][] arrangement,
      int tx, int ty, int layer)
  {
    this.arrangement = duplicatePermissions(arrangement);
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
  
  public void setPermissionAt(Permission perm, int tx, int ty)
  {
    arrangement[ty][tx] = perm;
  }
 
  
  private Permission[][] duplicatePermissions(Permission[][] original)
  {
    Permission[][] duplicate = 
        new Permission[original.length][original[0].length];
    
    for (int i = 0; i < original.length; i++)
    {
      for (int j = 0; j < original[i].length; j++)
      {
        duplicate[i][j] = original[i][j];
      }
    }
    
    return duplicate;
  }
  
  public PermissionArrangement duplicate()
  {
    return new PermissionArrangement(arrangement, tx, ty, layer);
  }
  
  public void applyAllPermissionsOf(PermissionArrangement apply)
  {
    
    for (int i = apply.getTileY(); i < apply.getTileY() + apply.getHeight(); i++)
    {
      for (int j = apply.getTileX(); j < apply.getTileX() + apply.getWidth(); j++)
      {
        arrangement[i - ty][j - tx] = apply.getPermissionAt(i - apply.getTileY(), 
            j - apply.getTileX());
      }
    }
  }
  
  public Permission[][] getPermissions()
  {
    return arrangement;
  }
}
