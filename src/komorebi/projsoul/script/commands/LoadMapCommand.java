package komorebi.projsoul.script.commands;

import java.io.File;

import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;
import komorebi.projsoul.states.Game;

public class LoadMapCommand extends CommandNoSubject {

  private String map;
  
  public static String keyword()
  {
    return "load";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException
  {
    map = data + ".map";
    
    if (!mapExists(map))
      throw new InvalidScriptSyntaxException(map + " cannot be found"); 
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
