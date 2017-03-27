package komorebi.projsoul.script.commands;

import komorebi.projsoul.engine.Main;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class FreezeCommand extends CommandNoSubject {

  int freezeFor;
  
  public static String keyword()
  {
    return "freeze";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    try {
      freezeFor = tryParse(data);
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public void execute() {
    Main.getGame().pause(freezeFor, lock);

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
