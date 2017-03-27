package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCOnly;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class PauseCommand extends CommandOnNPCOnly {

  private int pauseFor;
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    try
    {
      pauseFor = Integer.parseInt(data);
      
    } catch (NumberFormatException e)
    {
      throw new InvalidScriptSyntaxException(data + " cannot be resolved to an " + 
          "integer");
    }
    
  }

  @Override
  public void execute(NPC npc) {    
    npc.pause(pauseFor, lock);
    
  }
  
  public static String keyword()
  {
    return "pause";
  }
  

}
