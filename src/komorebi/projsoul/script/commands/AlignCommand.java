package komorebi.projsoul.script.commands;

import java.util.NoSuchElementException;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.Alignment;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCAndPlayer;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

public class AlignCommand extends CommandOnNPCAndPlayer {

  private Alignment alignment; 
  private static Keyword keyword;
  
  public static Keyword keyword()
  {
    return keyword;
  }
  
  public static void loadKeyword()
  {
    try
    {
      keyword = new Keyword("align", AlignCommand.class.getConstructor(),
          false);
    } catch (NoSuchMethodException e)
    {
      throw new UndefinedConstructorException("The align command must " +
          "define a constructor with no parameters");
    }
  }

  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {
    try {
      alignment = Alignment.getAlignment(data);
    } catch (NoSuchElementException e) {
      throw new InvalidScriptSyntaxExceptionWithLine("Invalid direction: " + data,
          line);
    }
  }

  @Override
  public void execute(NPC npc, Player player) {
    if (alignment.alignsToNPC())
      player.align(npc);
    else
      player.align(alignment.getDirection());
    
  }
  
}
