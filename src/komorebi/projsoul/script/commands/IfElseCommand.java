package komorebi.projsoul.script.commands;

import komorebi.projsoul.script.IfThenPair;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

public class IfElseCommand extends CommandNoSubject {

  private IfThenPair[] ifThen;
  
  @Override
  public void execute() {
    
    
  }

  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {
    String[] lines = data.split("\n");
    
    
  }
  
}
