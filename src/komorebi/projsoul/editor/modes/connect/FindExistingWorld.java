package komorebi.projsoul.editor.modes.connect;

public abstract class FindExistingWorld implements Runnable {

  private WorldShell world;
  
  @Override
  public void run() {
    
    Lock lock = new Lock();    
    FindExistingWorldDialog find = new FindExistingWorldDialog(lock);
    
    lock.lock();
    
    if (!find.wasCancelled())
    {
      world = find.getSelectedWorld();

      uponSuccess();      
      find.dispose();
    }

  }
  
  public WorldShell getSelectedWorld()
  {
    return world;
  }
  
  public abstract void uponSuccess();

}
