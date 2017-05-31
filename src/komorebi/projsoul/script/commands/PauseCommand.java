package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCOnly;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

public class PauseCommand extends CommandOnNPCOnly {

  private int pauseFor;
  
  @Override
  public void interpret(String data, int line) 
      throws InvalidScriptSyntaxExceptionWithLine {
    try
    {
      pauseFor = Integer.parseInt(data);
      
    } catch (NumberFormatException e)
    {
      throw new InvalidScriptSyntaxExceptionWithLine(data + " cannot be resolved to an " + 
          "integer", line);
    }
    
  }

  @Override
  public void execute(NPC npc) {    
    npc.pause(pauseFor);
    
  }

}
