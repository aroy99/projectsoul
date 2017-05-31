package komorebi.projsoul.script.commands.abstracts;

import komorebi.projsoul.entities.player.Player;

public abstract class CommandOnPlayerOnly extends Command {

  public abstract void execute(Player player);
}
