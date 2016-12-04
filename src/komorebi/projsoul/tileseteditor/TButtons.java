package komorebi.projsoul.tileseteditor;

public class TButtons {
  
  public enum TButtonType {
    OPEN(8, 2, 16, 0, false),
    SAVE(8, 1, 32, 0, false),
    SAVE_AS(8, 0, 48, 0, false),
    NEW(8, 3, 60, 0, false),
    GRID(8, 13, 76, 0, true),
    UNDO(8, 15, 165, 0, true),
    REDO(8, 14, 181, 0, true);
    
    
    private int x, y, texx, texy;
    private boolean hasAlt;
   
    private TButtonType(int x, int y, int texx, int texy, boolean alt)
    {
      this.x = x;
      this.y = y;
      this.texx = texx;
      this.texy = texy;
      
      hasAlt = alt;
    }
  }
  
  public enum TAccessoryType {
     
    CHECK(112,9,9,7,1f),
    X(112,1,7,7,1f),
    NEW_STAR(121,0,9,9,1f),
    HOVER_MARK(131,0,32,32,0.5f),
    GRID(96,0,16,16,1f);
    
    private int texx, texy, sx, sy;
    private float scale;
    
    private TAccessoryType(int texx, int texy, int sx, int sy, float scale)
    {
      
    }
  }
  
  public static class TAccessory {
    
  }
  

}
