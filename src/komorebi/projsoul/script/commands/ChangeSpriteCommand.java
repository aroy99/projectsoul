package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.NPCType;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCOnly;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;

public class ChangeSpriteCommand extends CommandOnNPCOnly {

  private NPCType newSprite;  
  
  public static String keyword()
  {
    return "change";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    
    boolean matchesSprite = false;
    for (String type: NPCType.allStrings())
    {
      if (data.equals(type))
      {
        newSprite = NPCType.toEnum(data);
        matchesSprite = true;
        break;
      }
    }
    
    if (!matchesSprite)
      throw new InvalidScriptSyntaxException("Cannot find sprite type: " +
          data);
    
  }

  @Override
  public void execute(NPC npc) {
    npc.setNPCType(newSprite);
  }

  
}
