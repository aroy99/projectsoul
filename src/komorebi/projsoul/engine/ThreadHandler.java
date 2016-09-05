/**
 * ThreadHandler.java		Aug 6, 2016, 2:32:04 PM
 */
package komorebi.projsoul.engine;

import java.util.ArrayList;

import komorebi.projsoul.script.Execution;
import komorebi.projsoul.script.Lock;
import komorebi.projsoul.script.Script;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class ThreadHandler {
  
  public static ArrayList<NewThread> threads = new ArrayList<NewThread>();
  
  public static class NewThread extends Thread
  {
    Script script;
    boolean interrupted;
    Lock lock;
    int waitIndex;
    
    public NewThread(Script script)
    {
      super(script.getExecution());
      
      this.script = script;
      interrupted = false;
      lock = null;
      waitIndex = 0;
    }
    
    public NewThread(Execution ex)
    {
      super(ex);
      interrupted = false;
      lock = null;
      waitIndex = 0;
    }
    
    public Script getScript()
    {
      return script;
    }
    
    public void setInterrupted(boolean b)
    {
      interrupted = b;
    }
    
    public boolean flagged()
    {
      return interrupted;
    }
    
    public void setLock(Lock lock)
    {
      this.lock = lock;
    }
    
    public Lock getLock()
    {
      return lock;
    }
  }
  
  
  public static void newThread(Script script)
  {
    NewThread thr = new NewThread(script);
    threads.add(thr);
    thr.start();
  }
  
  public static void newThread(NewThread thread)
  {
    threads.add(thread);
    thread.start();
  }
  
  public static void remove(Script script)
  {     
    int i = -1;
    
    for (int j=0; j<threads.size(); j++)
    {
      if (threads.get(j).getScript()==script)
      {
        i = j;
      }
    }
    
    if (i!=-1) threads.remove(i);
  }
  
  public static void remove(NewThread thread)
  {    
    int i = -1;
    
    for (int j=0; j<threads.size(); j++)
    {
      if (threads.get(j) == thread)
      {
        i = j;
      }
    }
    
    if (i!=-1) threads.remove(i);
  }
  
  public static void interrupt(Script script)
  {    
    System.out.println(threads.size());
    
    for (NewThread t: threads)
    {
      System.out.println(t);
    }
    
    for (int j=0; j<threads.size(); j++)
    {
      if (threads.get(j).getScript() == script)
      {
        threads.get(j).setInterrupted(true);
        break;
      }
    }
    
  }
  
  public static void giveLock(NewThread thread, Lock lock)
  {
    int i = threads.indexOf(thread);
    
    if (i!=-1) 
    {
      threads.get(i).setLock(lock);
    }
   }
  
  public static void unlock(Script script)
  {
    int i = -1;
    
    for (int j=0; j<threads.size(); j++)
    {
      if (threads.get(j).getScript()==script)
      {
        i = j;
      }
    }
    
    if (i!=-1)
    {
      if (threads.get(i).getLock()!=null)
      {
        threads.get(i).getLock().resumeThread();
      }
      
      threads.get(i).setInterrupted(false);
    }
  }
  
  public static int indexOf(Thread thread)
  {
    return threads.indexOf(thread);
  }
}
