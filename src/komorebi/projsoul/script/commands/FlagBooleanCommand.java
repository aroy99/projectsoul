package komorebi.projsoul.script.commands;

import komorebi.projsoul.engine.Main;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

public class FlagBooleanCommand extends CommandNoSubject {

  private int flag;

  @Override
  public void interpret(String data, int line) throws 
    InvalidScriptSyntaxExceptionWithLine {
    try {
      flag = tryParse(data, line);
    } catch (Exception e) {
      throw e;
    }
    
    if (flag < 0 || flag > 255)
      throw new InvalidScriptSyntaxExceptionWithLine("Boolean flag values must be " 
          + "between 0 and 255 (inclusive)", line);
  }

  @Override
  public void execute() {
    Main.getGame().setFlag(flag, true);
  }
  
  private int tryParse(String number, int line) throws InvalidScriptSyntaxExceptionWithLine
  {
    try {
      return Integer.parseInt(number);
    } catch (NumberFormatException e)
    {
      throw new InvalidScriptSyntaxExceptionWithLine(number + " cannot be "
          + "resolved to an integer", line);
    }
  }
}
