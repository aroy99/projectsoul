package komorebi.projsoul.script.commands;

import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.states.Game;

public class UnblockCommand extends CommandNoSubject {
  
  private int x, y;

  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {
    
    String[] nums = data.split(" ");
    
    if (nums.length != 2)
      throw new InvalidScriptSyntaxExceptionWithLine("The block command"
          + " takes only two arguments (an x, y coordinate)", line);
    
    try {
      x = tryParse(nums[0], line);
      y = tryParse(nums[1], line);
    } catch (Exception e)
    {
      throw e;
    }
  }

  @Override
  public void execute() {
    Game.getMap().setCollision(x, y, true);
    
  }

}
