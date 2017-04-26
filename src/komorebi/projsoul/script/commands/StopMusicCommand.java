package komorebi.projsoul.script.commands;

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

public class StopMusicCommand extends CommandNoSubject {
  
  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {
    if (!data.isEmpty())
      throw new InvalidScriptSyntaxExceptionWithLine("The stop music command "
          + "takes no arguments", line);
  }

  @Override
  public void execute() {
    AudioHandler.stop();
  }
}
