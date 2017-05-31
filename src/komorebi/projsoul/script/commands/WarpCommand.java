package komorebi.projsoul.script.commands;

import java.io.File;

import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.commands.abstracts.CommandOnPlayerOnly;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.states.Game;

public class WarpCommand extends CommandOnPlayerOnly {

  private String map;
  private int x, y;

  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine
  {
    String[] args = data.split(" ");
    
    if (args.length != 3)
    {
      throw new InvalidScriptSyntaxExceptionWithLine("The warp command"
          + " requires 3 arguments (map, x, y)", line);
    }

    map = args[0] + ".map";

    if (!mapExists(map))
      throw new InvalidScriptSyntaxExceptionWithLine(map + " cannot be found", line); 

    try {
      x = tryParse(args[1], line);
      y = tryParse(args[2], line);
    } catch (Exception e)
    {
      throw e;
    }
  }

  private boolean mapExists(String map)
  {
    File folder = new File("res/maps");

    for (File file: folder.listFiles())
      if (file.getName().equals(map))
        return true;

    return false;
  }

  @Override
  public void execute(Player player) {
    //TODO FIX
//    Map oldMap = Game.getMap();
//    Map newMap = Map.createMapAndTransferPlayers(
//        "res/maps/" + map, oldMap);
    
//    Game.setMap(newMap);
       
    player.setTileLocation(x, y);
  }

}
