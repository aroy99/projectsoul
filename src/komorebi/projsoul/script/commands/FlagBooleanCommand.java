package komorebi.projsoul.script.commands;

import komorebi.projsoul.engine.Main;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class FlagBooleanCommand extends CommandNoSubject {

  private int flag;
  
  public static String keyword()
  {
    return "flag";
  }

  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    try {
      flag = tryParse(data);
    } catch (Exception e) {
      throw e;
    }
    
    if (flag < 0 || flag > 255)
      throw new InvalidScriptSyntaxException("Boolean flag values must be " 
          + "between 0 and 255 (inclusive)");
  }

  @Override
  public void execute() {
    Main.getGame().setFlag(flag, true);
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
