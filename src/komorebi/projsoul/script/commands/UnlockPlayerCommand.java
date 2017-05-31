package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCAndPlayer;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;

public class UnlockPlayerCommand extends CommandOnNPCAndPlayer {

  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {
    if (!data.isEmpty())
      throw new InvalidScriptSyntaxExceptionWithLine("The unlock command "
          + "takes no arguments", line);
    
  }

  @Override
  public void execute(NPC npc, Player player) {
    player.unlock();
    npc.disengage();  
  }

}
