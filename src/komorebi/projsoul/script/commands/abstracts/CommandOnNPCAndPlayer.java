package komorebi.projsoul.script.commands.abstracts;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;

public abstract class CommandOnNPCAndPlayer extends Command {

  public abstract void execute(NPC npc, Player player);
}
