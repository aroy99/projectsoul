/**
 * ThreadHandler.java  Aug 6, 2016, 2:32:04 PM
 */
package komorebi.projsoul.engine;

import komorebi.projsoul.script.Execution;
import komorebi.projsoul.script.Lock;
import komorebi.projsoul.script.Script;

import java.util.ArrayList;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class ThreadHandler {
  
  public static ArrayList<NewThread> threads = new ArrayList<NewThread>();
  public static ArrayList<Lock> stragglers = new ArrayList<Lock>();
  
  /**
   * A thread object (extending java's Thread class) that can
   * interact with the ThreadHandler class
   * 
   * @author Andrew Faulkenberry
   */
  public static class NewThread extends Thread
  {
    Script script;
    boolean interrupted;
    Lock lock;
    int waitIndex;
    
    /**
     * Creates a new thread that will run the script's instructions when the
     * Thread.start() method is called
     * @param script The script to run on a separate thread
     */
    public NewThread(Script script)
    {
      super(script.getExecution());
      
      this.script = script;
      interrupted = false;
      lock = null;
      waitIndex = 0;
    }
    
    /**
     * Creates a new thread that will run the execution's instructions when the
     * Thread.start() method is called
     * @param ex The execution to run on a separate thread
     */
    public NewThread(Execution ex)
    {
      super(ex);
      interrupted = false;
      lock = null;
      waitIndex = 0;
    }
    
    /**
     * @return The script associated with the Thread (or null if the Thread was
     * not instantiated with a script)
     */
    public Script getScript()
    {
      return script;
    }
    
    /**
     * @param b If true, halts the thread; if false, resumes the thread
     */
    public void setInterrupted(boolean b)
    {
      interrupted = b;
    }
    
    /** 
     * @return Whether the thread is currently halted
     */
    public boolean flagged()
    {
      return interrupted;
    }
    
    /**
     * Sets the Thread's lock object
     * @param lock The lock object to be set
     */
    public void setLock(Lock lock)
    {
      this.lock = lock;
    }
    
    /**
     * @return The thread's current lock object
     */
    public Lock getLock()
    {
      return lock;
    }
  }
  
  /**
   * Creates and beings a new thread which will run the instructions of the
   * script
   * @param script 
   */
  public static void newThread(Script script)
  {
    NewThread thr = new NewThread(script);
    
    System.out.println("Added: " + thr);
    
    threads.add(thr);
    thr.start();
  }
  
  /**
   * Adds a given NewThread to 
   * @param thread
   */
  public static void newThread(NewThread thread)
  {
    System.out.println("Added: " + thread);
    
    threads.add(thread);
    thread.start();
  }
  
  public static void remove(Script script)
  {
    int i = -1;
    
    for (int j=0; j < threads.size(); j++)
    {
      if (threads.get(j).getScript() == script)
      {
        i = j;
      }
    }
    
    if (i != -1){
      NewThread dead = threads.remove(i);
      /*
      System.out.println("removed " + dead);
      dead.setInterrupted(true);
      dead.interrupt();
      try {
        dead.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
        System.err.println("Successfully murdered thread");
      }
      */
    }
  }
  
  public static void remove(NewThread thread)
  {
    int i = -1;
    
    for (int j=0; j < threads.size(); j++)
    {
      if (threads.get(j) == thread)
      {
        i = j;
      }
    }
    
    if (i != -1){
      NewThread dead = threads.remove(i);
      /*
      System.out.println("removed " + dead);
      dead.setInterrupted(true);
      dead.interrupt();
      try {
        dead.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
        System.err.println("Successfully murdered thread");
      }
      */
    }
  }
  
  public static void interrupt(Script script)
  {
    System.out.println(threads.size());
    
    //DEBUG Print threads
    for (NewThread t: threads)
    {
      System.out.println(t);
    }
    
    for (int j=0; j < threads.size(); j++)
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
    
    if (i != -1) 
    {
      threads.get(i).setLock(lock);
    }
  }
  
  public static void unlock(Script script)
  {
    int i = -1;
    
    for (int j=0; j < threads.size(); j++)
    {
      if (threads.get(j).getScript() == script)
      {
        i = j;
      }
    }
    
    if (i != -1)
    {
      if (threads.get(i).getLock() != null)
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
