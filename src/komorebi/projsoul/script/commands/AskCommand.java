package komorebi.projsoul.script.commands;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCOnly;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;
import komorebi.projsoul.script.read.Branch;

public class AskCommand extends CommandOnNPCOnly {

  private String[] branchNames;
  private Branch[] branches;
  
  public static String keyword()
  {
    return "ask";
  }
  
  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
   
    String[] words = data.split("\"");

    branchNames = new String[words.length/2];

    try {
      for (int i=0; i < branchNames.length; i++)
      {
        branchNames[i] = words[(i*2)+1];
      }
    } catch (Exception e)
    {
      throw new InvalidScriptSyntaxException("Invalid ask command syntax");
    }

  }
  
  private Branch getBranchWithName(String name)
  {
    for (Branch branch: branches)
    {
      if (branch.getName().equals(name))
        return branch;
    }
    
    return null;
  }

  @Override
  public void execute(NPC npc) {
    String[] sayThese = new String[branchNames.length];
    
    for (int i = 0; i < branchNames.length; i++)
    {
      if (i == 0)
      {
        sayThese[i] = branchNames[i].replace("@me", 
            Map.getPlayer().getCharacter().getName());
      } else
      {
        sayThese[i] = branchNames[i];
      }
    }
    
    String answer = npc.ask(sayThese, lock);
    
    Branch runThisBranch = getBranchWithName(answer);
    //(new Execution(npc, runThisBranch)).run();
    
  }
  
  

}
