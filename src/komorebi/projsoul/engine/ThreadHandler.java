/**
 * ThreadHandler.java  Aug 6, 2016, 2:32:04 PM
 */
package komorebi.projsoul.engine;

import java.util.ArrayList;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.execute.Execution;
import komorebi.projsoul.script.execute.LoopableExecution;
import komorebi.projsoul.script.read.Branch;
import komorebi.projsoul.script.tasks.Task.Precedence;
import komorebi.projsoul.script.utils.ScriptDatabase;

/**
 * 
 * @author Aaron Roy
 * @version 
 */
public class ThreadHandler {
    
  private static ArrayList<TrackableThread> threads = new
      ArrayList<TrackableThread>();
  
  public static class TrackableThread extends Thread {
    private Precedence precedence;
    
    protected TrackableThread(Execution ex, Precedence prec)
    {
      super(ex);
      precedence = prec;
      threads.add(this);
    }
    
    public Precedence precedence()
    {
      return precedence;
    }
  }
  
  public static TrackableThread currentThread()
  {
    if (threads.contains(Thread.currentThread()))
      return (TrackableThread) Thread.currentThread();
    
    throw new RuntimeException("Current thread is not a trackable thread");
  }
  
  public static void newThread(String script, NPC npc, Player player)
  {
    Execution ex = ScriptDatabase.newExecution(script);
    ex.setOnWhom(npc, player);
    run(ex, Precedence.FOREGROUND);    
  }
 
  public static void newThread(Branch branch, NPC npc,
      Player player)
  {
    Execution ex = new Execution(branch);
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
}
