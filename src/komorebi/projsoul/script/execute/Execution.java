/**
 * Execution.java Jun 11, 2016, 11:58:10 AM
 */
package komorebi.projsoul.script.execute;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.commands.abstracts.Command;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.commands.abstracts.CommandOnAnyPerson;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCAndPlayer;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCOnly;
import komorebi.projsoul.script.commands.abstracts.CommandOnPlayerOnly;
import komorebi.projsoul.script.read.Branch;
import komorebi.projsoul.script.tasks.Task.Precedence;

/**
 * 
 * @author Andrew Faulkenberry
 * @version 
 */
public class Execution implements Runnable {

  private enum ScriptType {
    WALKING(Precedence.BACKGROUND), TALKING(Precedence.FOREGROUND);

    
    private Precedence precedence;
    
    private ScriptType(Precedence precedence)
    {
      this.precedence = precedence;
    }
    
    public Precedence precedence()
    {
      return precedence;
    }
    
    public static ScriptType fromIsWalking(boolean isWalking)
    {
      if (isWalking)
        return WALKING;
      
      return TALKING;
    }
  }
  
  private Branch branch;
  
  protected NPC npc;
  private Player play;
  private ScriptType scriptType;
  
  /**
   * Creates an execution object which will begin on the given branch
   * @param myNpc The NPC the execution should affect
   * @param toDo The list of instructions to be executed on the new thread
   */
  public Execution(Branch branch)
  {
    this.branch = branch;
    scriptType = ScriptType.fromIsWalking(this 
        instanceof LoopableExecution);
  }
  
  public void setOnWhom(NPC npc, Player play)
  {
    this.npc = npc;
    this.play = play;
  }

  public Precedence precedence()
  {
    return scriptType.precedence();
  }

  @Override
  public void run() {
                
    for (Command command: branch)
    {                                    
      if (command instanceof CommandNoSubject)
        ((CommandNoSubject) command).execute();
      else if (command instanceof CommandOnAnyPerson)
        if (command.appliesToPlayer())
          ((CommandOnAnyPerson) command).execute(play);
        else
          ((CommandOnAnyPerson) command).execute(npc);
      else if (command instanceof CommandOnNPCAndPlayer)
        ((CommandOnNPCAndPlayer) command).execute(npc, play);
      else if (command instanceof CommandOnNPCOnly)
        ((CommandOnNPCOnly) command).execute(npc);
      else if (command instanceof CommandOnPlayerOnly)
        ((CommandOnPlayerOnly) command).execute(play);
    }
    
  }


}
