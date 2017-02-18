package komorebi.projsoul.editor.history;

import komorebi.projsoul.editor.Sublayer;
import komorebi.projsoul.engine.Draw;

public class TileRevision extends Revision {

  private int tx, ty;
  
  private Sublayer sub; //the sublayer in which this revision was made
  
  private int[][] preTiles; //what the tiles were before
  private int[][] postTiles; //what they are after
  
  /**
   * Represents any revision of the tiles on the map, 
   *  an act which can be undone and redone
   * @param preTiles The edited tiles prior to the edit
   * @param postTiles The edited tiles after the edit
   * @param tx The bottom left tile's x
   * @param ty The bottom left tile's y
   * @param sub The sublayer in which the revision was made
   */
  public TileRevision(int[][] preTiles, int[][] postTiles, int tx, 
      int ty, Sublayer sub)
  {
    super();
    
    this.preTiles = preTiles;
    this.postTiles = postTiles;
    this.tx = tx;
    this.ty = ty;
    this.sub = sub;
    
    description = "Edited " + sub.getTextField().getText();
  }

  @Override
  public void undo() {
    
    for (int i = ty; i < ty + preTiles.length; i++)
    {
      for (int j = tx; j < tx + preTiles[0].length; j++)
      {
        sub.getTiles()[i][j] = preTiles[i-ty][j-tx];
      }
    }
    
  }

  @Override
  public void redo() {
    for (int i = ty; i < ty + postTiles.length; i++)
    {
      for (int j = tx; j < tx + postTiles[0].length; j++)
      {
        sub.getTiles()[i][j] = postTiles[i-ty][j-tx];
      }
    }
    
  }

  @Override
  public void render() {
    Draw.drawIfInBounds(Draw.LAYER_MANAGER, clickableArea.x, 
        clickableArea.y, 16, 16, 0, 32, 16, 48, 2);
    
  }
  
}
