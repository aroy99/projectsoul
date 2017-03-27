package komorebi.projsoul.script.commands;

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class StopMusicCommand extends CommandNoSubject {

  public static String keyword()
  {
    return "stop music";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    if (!data.isEmpty())
      throw new InvalidScriptSyntaxException("The stop music command "
          + "takes no arguments");
  }

  @Override
  public void execute() {
    AudioHandler.stop();
  }
}
