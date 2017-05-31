package komorebi.projsoul.script.commands;

import java.awt.Point;
import java.io.File;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCAndPlayer;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.states.Game;

public class WarpTogetherCommand extends CommandOnNPCAndPlayer {

  private Point npcTo, playerTo;
  private String map;
  
  @Override
  public void execute(NPC npc, Player player) {
    //TODO FIX
//    Map oldMap = Game.getMap();
//    Map newMap = Map.createMapAndTransferPlayers(
//        "res/maps/" + map, oldMap);
    
//    Game.setMap(newMap);
       
    player.setTileLocation(playerTo.x, playerTo.y);
    npc.setTileLocation(npcTo.x, npcTo.y);
    
//    newMap.addNPC(npc);

  }

  @Override
  public void interpret(String data, int line)
      throws InvalidScriptSyntaxExceptionWithLine {

    String[] args = data.split(" ");
    
    if (args.length != 5)
    {
      throw new InvalidScriptSyntaxExceptionWithLine("The warp command"
          + " requires 5 arguments: map, player (x, y), NPC (x, y)", line);
    }

    map = args[0] + ".map";

    if (!mapExists(map))
      throw new InvalidScriptSyntaxExceptionWithLine(map + " cannot be found", line); 

    try {
      int playx = tryParse(args[1], line);
      int playy = tryParse(args[2], line);
      int npcx = tryParse(args[3], line);
      int npcy = tryParse(args[4], line);
      
      npcTo = new Point(npcx, npcy);
      playerTo = new Point(playx, playy);
      
    } catch (InvalidScriptSyntaxException e)
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

}
