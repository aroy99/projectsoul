package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCOnly;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;

public class GoToCommand extends CommandOnNPCOnly {

  private int x, y;

  @Override
  public void interpret(String data, int line) 
      throws InvalidScriptSyntaxExceptionWithLine {
    String[] args = data.split(" ");
    
    try {
      x = tryParse(args[0], line);
      y = tryParse(args[1], line);
    } catch (Exception e)
    {
      throw e;
    }
  }

  @Override
  public void execute(NPC npc) {
    npc.goToPixX(x*16);
    npc.goToPixY(y*16);

  }

}
