package komorebi.projsoul.script.commands.abstracts;

import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.read.Request;

public abstract class Command {

  private boolean appliesToPlayer;
  
  public void setApplicableToPlayer(boolean b)
  {
    appliesToPlayer = b;
  }
  
  public Request[] askForRequests()
  {
    return new Request[0];
  }
 
  public boolean appliesToPlayer()
  {
    return appliesToPlayer;
  }
  
  protected int tryParse(String number, int line) 
      throws InvalidScriptSyntaxExceptionWithLine
  {
    try {
      return Integer.parseInt(number);
    } catch (NumberFormatException e)
    {
      throw new InvalidScriptSyntaxExceptionWithLine(number + 
          " cannot be resolved to an integer", line);
    }
  }
  
  public abstract void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine;
}
