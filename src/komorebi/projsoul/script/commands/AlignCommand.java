package komorebi.projsoul.script.commands;

import java.util.NoSuchElementException;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.Alignment;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCAndPlayer;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class AlignCommand extends CommandOnNPCAndPlayer {

  private Alignment alignment; 
  
  public static String keyword()
  {
    return "align";
  }

  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    try {
      alignment = Alignment.getAlignment(data);
    } catch (NoSuchElementException e) {
      throw new InvalidScriptSyntaxException("Invalid direction: " + data);
    }
  }

  @Override
  public void execute(NPC npc, Player player) {
    if (alignment.alignsToNPC())
      player.align(npc, lock);
    else
      player.align(alignment.getDirection(), lock);
    
  }
  
}
