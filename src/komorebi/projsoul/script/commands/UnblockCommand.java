package komorebi.projsoul.script.commands;

import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;
import komorebi.projsoul.states.Game;

public class UnblockCommand extends CommandNoSubject {
  
  private int x, y;
  
  public static String keyword()
  {
    return "unblock";
  }

  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    
    String[] nums = data.split(" ");
    
    try {
      x = tryParse(nums[0]);
      y = tryParse(nums[1]);
    } catch (Exception e)
    {
      throw e;
    }
  }

  @Override
  public void execute() {
    Game.getMap().setCollision(x, y, true);
    
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
