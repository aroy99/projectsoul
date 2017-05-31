package komorebi.projsoul.script.read;

import java.util.ArrayList;

import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class ErrorLog {

  private ArrayList<InvalidScriptSyntaxException> errors;

  public ErrorLog()
  {
    errors = new ArrayList<InvalidScriptSyntaxException>();
  }

  public void addError(InvalidScriptSyntaxException e)
  {
    errors.add(e);
  }
  
  public void addAllErrorsIn(ArrayList<InvalidScriptSyntaxException> add)
  {
    for (InvalidScriptSyntaxException e: add)
    {
      errors.add(e);
    }
  }

  public String getErrors()
  {
    String all = "";

    for (InvalidScriptSyntaxException e: errors)
    {
      all += e.getMessage() + "\n";
    }

    return all;
  }

}
