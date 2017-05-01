/**
 * ThreadHandler.java  Aug 6, 2016, 2:32:04 PM
 */
package komorebi.projsoul.engine;

import java.util.ArrayList;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.commands.abstracts.Command;
import komorebi.projsoul.script.execute.Execution;
import komorebi.projsoul.script.execute.LoopableExecution;
import komorebi.projsoul.script.execute.SubExecution;
import komorebi.projsoul.script.read.Branch;
import komorebi.projsoul.script.tasks.Task.Precedence;
import komorebi.projsoul.script.utils.ScriptDatabase;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class ThreadHandler {
    
  private static ArrayList<TrackableThread> background = new
      ArrayList<TrackableThread>();
  
  public static class TrackableThread extends Thread {
    private Precedence precedence;
    private boolean terminated;
    
    private Lock lock;
    
    protected TrackableThread(Execution ex, Precedence prec)
    {
      super(ex);
      precedence = prec;
      
      if (precedence == Precedence.BACKGROUND)
        background.add(this);
      
      this.lock = new Lock();
    }
    
    public Precedence precedence()
    {
      return precedence;
    }
    
    public void terminate()
    {
      terminated = true;
    }
    
    public boolean isTerminated()
    {
      return terminated;
    }
    
    public void lock()
    {
      lock.lock(1);
    }
    
    public void multiLock(int keys)
    {
      lock.lock(keys);
    }
    
    public void unlock()
    {
      lock.unlock();
    }
  }
  
  private static class Lock {
    
    private int keys;
    
    /**
     * Pauses the current thread
     */
    public void lock(int keys)
    {          
      synchronized (this)
      {
        this.keys = keys;
        
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
        keys--;
        
        if (keys <= 0)
          notifyAll();
      }
    }
  }

  
  public static void endBackgroundThreads()
  {
    for (TrackableThread thread: background)
    {
      thread.terminate();
    }
  }
  
  public static TrackableThread currentThread()
  {
    if (Thread.currentThread() instanceof TrackableThread)
      return (TrackableThread) Thread.currentThread();
    
    throw new RuntimeException("Current thread is not a trackable thread");
  }
  
  public static void newThread(String script, NPC npc, Player player)
  {
    Execution ex = ScriptDatabase.newExecution(script);
    ex.setOnWhom(npc, player);
    run(ex, Precedence.FOREGROUND);    
  }
 
  public static void branchInto(Branch branch, NPC npc,
      Player player, TrackableThread returnTo)
  {
    SubExecution ex = new SubExecution(branch, returnTo);
    ex.setOnWhom(npc, player);
    
    run(ex, Precedence.FOREGROUND);
  }
  
  public static void newLoop(String script, NPC npc, Player player)
  {
    LoopableExecution loop = ScriptDatabase.newLoopable(script);
    loop.setOnWhom(npc, player);
    run(loop, Precedence.BACKGROUND);
  }

  
  private static void run(Execution ex, Precedence precedence)
  {            
    new TrackableThread(ex, precedence).start();
  }
  
  private static void runOnCurrentThread(Execution ex)
  {
    ex.run();
  }
  
  public static void executeOnCurrentThread(Command command, NPC npc,
      Player player)
  {
    Branch branch = new Branch("");
    branch.addTask(command);
    
    executeOnCurrentThread(branch, npc, player);
  }
  
  public static void executeOnCurrentThread(Branch branch, NPC npc,
      Player player)
  {    
    Execution ex = new Execution(branch);
    ex.setOnWhom(npc, player);
    
    runOnCurrentThread(ex);
  }
  
  public static void lockCurrentThread(int keys)
  {
    try
    {
      currentThread().multiLock(keys);
    } catch (RuntimeException e)
    {
      throw new RuntimeException("Cannot pause the main thread");
    }
  }
  
  public static void lockCurrentThread()
  {
    lockCurrentThread(1);
  }
}
