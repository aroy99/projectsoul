package komorebi.projsoul.editor.modes.connect;

public abstract class CreateNewWorld implements Runnable {

  private WorldShell world;
  private String mapToAdd;
  
  public CreateNewWorld(String mapToAdd)
  {
    this.mapToAdd = mapToAdd;
  }
  
  @Override
  public void run() {
    Lock lock = new Lock();    
    CreateNewWorldDialog find = new CreateNewWorldDialog(mapToAdd, lock);
        
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
