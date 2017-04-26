package komorebi.projsoul.script.commands;

import java.io.File;

import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;
import komorebi.projsoul.states.Game;

public class LoadMapCommand extends CommandNoSubject {

  private String map;

  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine
  {
    map = data + ".map";
    
    if (!mapExists(map))
      throw new InvalidScriptSyntaxExceptionWithLine(map + " cannot be found", line); 
  }

  @Override
  public void execute() {
    Game.setMap(new Map("res/maps/" + map));
    
  }
  
  private boolean mapExists(String map)
  {
    File folder = new File("res/maps");
    
    for (File file: folder.listFiles())
      if (file.getName().equals(map))
        return true;
    
    return false;
  }
 
}
