package komorebi.projsoul.script.commands;

import komorebi.projsoul.script.IfThenPair;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class IfElseCommand extends CommandNoSubject {

  private IfThenPair[] ifThen;
  
  public static String keyword()
  {
    return "if";
  }

  @Override
  public void execute() {
    
    
  }

  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    String[] lines = data.split("\n");
    
    
  }
  
}
