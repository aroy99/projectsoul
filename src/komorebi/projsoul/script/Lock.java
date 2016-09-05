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

  public void pauseThread()
  {    
    synchronized (this)
    {
      try
      {
        wait();
      } catch (InterruptedException e)
      {
        
      }
    }
  }
  
  public void resumeThread()
  {
    synchronized (this)
    {
      notifyAll();
    }
  }
}
