package komorebi.projsoul.script.commands.abstracts;

import komorebi.projsoul.entities.NPC;

public abstract class CommandOnNPCOnly extends Command {
  
  public abstract void execute(NPC npc);

}
