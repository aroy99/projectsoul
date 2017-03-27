package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCAndPlayer;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class UnlockPlayerCommand extends CommandOnNPCAndPlayer {

  public static String keyword()
  {
    return "unlock";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    if (!data.isEmpty())
      throw new InvalidScriptSyntaxException("The unlock command "
          + "takes no arguments");
    
  }

  @Override
  public void execute(NPC npc, Player player) {
    player.unlock();
    npc.disengage();  
  }

}
