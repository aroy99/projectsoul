package komorebi.projsoul.script.commands;

import java.util.NoSuchElementException;

import komorebi.projsoul.audio.AudioHandler;
import komorebi.projsoul.audio.Song;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class PlayMusicCommand extends CommandNoSubject {

  private Song song;
  
  public static String keyword()
  {
    return "play";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException
  {
    try {
      song = Song.get(data);
    } catch (NoSuchElementException e) {
      throw new InvalidScriptSyntaxException("There is no such song as "
          + data);
    }
  }

  @Override
  public void execute() {
    AudioHandler.play(song);

  }

}
