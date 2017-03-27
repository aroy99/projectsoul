package komorebi.projsoul.script.commands;

import komorebi.projsoul.script.Fader;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class FadeoutCommand extends CommandNoSubject {

  public static String keyword()
  {
    return "fadeout";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    if (!data.isEmpty())
      throw new InvalidScriptSyntaxException("The fadeout command "
          + "takes no arguments");
  }

  @Override
  public void execute() {
    Fader.fadeOut(lock);
  }

}
