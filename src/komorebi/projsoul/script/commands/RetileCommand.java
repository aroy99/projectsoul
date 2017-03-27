package komorebi.projsoul.script.commands;

import komorebi.projsoul.map.TileList;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;
import komorebi.projsoul.states.Game;

public class RetileCommand extends CommandNoSubject {

  private int newTile, x, y;
  
  public static String keyword()
  {
    return "retile";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    
    String[] args = data.split(" ");
    
    try {
      newTile = tryParse(args[0]);
      x = tryParse(args[1]);
      y = tryParse(args[2]);
    } catch (Exception e)
    {
      throw e;
    }
  }

  @Override
  public void execute() {
    Game.getMap().setTile(TileList.getTile(newTile), 
        x, y);

  }
  
  private int tryParse(String number) throws InvalidScriptSyntaxException
  {
    try {
      return Integer.parseInt(number);
    } catch (NumberFormatException e)
    {
      throw new InvalidScriptSyntaxException(number + " cannot be "
          + "resolved to an integer");
    }
  }

}
