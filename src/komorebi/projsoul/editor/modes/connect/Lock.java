package komorebi.projsoul.editor.modes.connect;

public class Lock {
    
  /**
   * Pauses the current thread
   */
  public void lock()
  {          
    synchronized (this)
    {      
      try
      {
        wait();
      } catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  /**
   * Resumes the thread originally paused by this lock
   */
  public void unlock()
  {          
    synchronized (this)
    {
      notifyAll();
    }
  }
}