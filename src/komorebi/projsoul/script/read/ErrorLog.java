package komorebi.projsoul.script.read;

import java.io.File;
import java.util.ArrayList;

import komorebi.projsoul.script.commands.keywords.Keywords;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;
import komorebi.projsoul.script.exceptions.UndefinedKeywordException;
import komorebi.projsoul.script.utils.Script;

public class ErrorLog {

  private ArrayList<InvalidScriptSyntaxException> errors;

  public static void main(String[] args)
  {
    Keywords.loadKeywords();

    Script script = new Script(new File("res/scripts/debug_script"));
    System.out.println(script.getErrors());

  }

  public ErrorLog()
  {
    errors = new ArrayList<InvalidScriptSyntaxException>();
  }

  public void addError(InvalidScriptSyntaxException e)
  {
    errors.add(e);
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
