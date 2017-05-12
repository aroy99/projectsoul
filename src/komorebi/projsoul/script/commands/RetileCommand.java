package komorebi.projsoul.script.commands;

import komorebi.projsoul.map.TileList;
import komorebi.projsoul.script.commands.abstracts.CommandNoSubject;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;
import komorebi.projsoul.states.Game;

public class RetileCommand extends CommandNoSubject {

  private int newTile, x, y;
  
  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {
    
    String[] args = data.split(" ");
    
    try {
      newTile = tryParse(args[0], line);
      x = tryParse(args[1], line);
      y = tryParse(args[2], line);
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
}
