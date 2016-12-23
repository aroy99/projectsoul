/**
 * Lock.java  Jun 11, 2016, 11:10:34 AM
 */
package komorebi.projsoul.script;

/**
 * 
 * @author Andrew Faulkenberry
 * @version 
 */
public class Lock {
  
  public Lock()
  {
    
  }

  /**
   * Pauses the current thread
   */
  public void pauseThread()
  {    
    synchronized (this)
    {
      try
      {
        wait();
      } catch (InterruptedException e)
      {
        //What went wrong? :(
      }
    }
  }
  
  /**
   * Resumes the thread originally paused by this lock
   */
  public void resumeThread()
  {
    synchronized (this)
    {
      notifyAll();
    }
  }
}
