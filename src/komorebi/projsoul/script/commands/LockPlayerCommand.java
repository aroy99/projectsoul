package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.commands.abstracts.CommandOnPlayerOnly;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

/**
 * 
 *
 * @author Andrew Faulkenberry
 */
public class LockPlayerCommand extends CommandOnPlayerOnly {
  
  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {
    if (!data.isEmpty()){
      throw new InvalidScriptSyntaxExceptionWithLine("The lock command " + 
                                                    "takes no arguments", line);
    }
    
  }

  @Override
  public void execute(Player player) {
    player.stop();
    player.lock();
  }
}
