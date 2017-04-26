package komorebi.projsoul.script.commands;

import komorebi.projsoul.script.Fader;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

public class FadeInCommand extends CommandNoSubject {
  
  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {
    if (!data.isEmpty())
      throw new InvalidScriptSyntaxExceptionWithLine("The fadein command "
          + "takes no arguments", line);
    
  }

  @Override
  public void execute() {
    Fader.fadeIn();
    
  }

}
