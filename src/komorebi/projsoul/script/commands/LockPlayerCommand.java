package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.commands.abstracts.CommandOnPlayerOnly;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class LockPlayerCommand extends CommandOnPlayerOnly {

  public static String keyword()
  {
    return "lock";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    if (!data.isEmpty())
      throw new InvalidScriptSyntaxException("The lock command "
          + "takes no arguments");
    
  }

  @Override
  public void execute(Player player) {
    player.stop();
    player.lock();
  }
}
