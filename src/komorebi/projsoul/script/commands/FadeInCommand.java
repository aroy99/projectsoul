package komorebi.projsoul.script.commands;

import komorebi.projsoul.script.Fader;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class FadeInCommand extends CommandNoSubject {

  public static String keyword()
  {
    return "fadein";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    if (!data.isEmpty())
      throw new InvalidScriptSyntaxException("The fadein command "
          + "takes no arguments");
    
  }

  @Override
  public void execute() {
    Fader.fadeIn(lock);
    
  }

}
