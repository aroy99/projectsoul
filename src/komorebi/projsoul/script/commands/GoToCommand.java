package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCOnly;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class GoToCommand extends CommandOnNPCOnly {

  private int x, y;
  
  public static String keyword()
  {
    return "goto";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    String[] args = data.split(" ");
    
    try {
      x = tryParse(args[0]);
      y = tryParse(args[1]);
    } catch (Exception e)
    {
      throw e;
    }
  }

  @Override
  public void execute(NPC npc) {
    npc.setTileLocation(x, y);

  }
  
  private int tryParse(String number) throws InvalidScriptSyntaxException
  {
    try {
      return Integer.parseInt(number);
    } catch (NumberFormatException e)
    {
      throw new InvalidScriptSyntaxException(number + " cannot be "
          + "resolved to an integer");
    }
  }

}
