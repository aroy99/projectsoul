package komorebi.projsoul.script.commands;

import komorebi.projsoul.engine.Main;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

public class FreezeCommand extends CommandNoSubject {

  int freezeFor;
 
  @Override
  public void interpret(String data, int line) 
      throws InvalidScriptSyntaxExceptionWithLine {
    try {
      freezeFor = tryParse(data, line);
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public void execute() {
    Main.getGame().pause(freezeFor);

  }
  
  private int tryParse(String number, int line) 
      throws InvalidScriptSyntaxExceptionWithLine
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
