
package komorebi.projsoul.engine;

public abstract class InitializableAnimation extends Animation {

  public InitializableAnimation(int f, int t, float sx, float sy, 
      int id) {
    super(f, t, sx, sy, id);
    
    initialize();
  }
  
  public InitializableAnimation(int f, int t, int id)
  {
    super(f, t, id);
    
    initialize();
  }
  
  public abstract void initialize();
  
}
