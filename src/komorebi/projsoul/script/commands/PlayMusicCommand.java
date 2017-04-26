package komorebi.projsoul.script.commands;

import java.util.NoSuchElementException;

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.audio.Song;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

public class PlayMusicCommand extends CommandNoSubject {

  private Song song;

  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine
  {
    try {
      song = Song.get(data);
    } catch (NoSuchElementException e) {
      throw new InvalidScriptSyntaxExceptionWithLine("There is no such song as "
          + data, line);
    }
  }

  @Override
  public void execute() {
    AudioHandler.play(song);

  }

}
