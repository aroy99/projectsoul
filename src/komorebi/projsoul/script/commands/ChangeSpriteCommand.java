package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCOnly;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;

public class ChangeSpriteCommand extends CommandOnNPCOnly {

  private NPCType newSprite;
  
  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {
    
    boolean matchesSprite = false;
    for (String type: NPCType.allStrings())
    {
      if (data.equalsIgnoreCase(type))
      {
        newSprite = NPCType.toEnum(data.toUpperCase());
        matchesSprite = true;
        break;
      }
    }
    
    if (!matchesSprite)
      throw new InvalidScriptSyntaxExceptionWithLine("Cannot find sprite type: " +
          data, line);
    
  }

  @Override
  public void execute(NPC npc) {
    npc.setNPCType(newSprite);
  }

  
}
