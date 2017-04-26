package komorebi.projsoul.script.commands;

import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCAndPlayer;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;
import komorebi.projsoul.script.read.Branch;
import komorebi.projsoul.script.read.CommandRequest;
import komorebi.projsoul.script.read.Request;

public class CallBranchCommand extends CommandOnNPCAndPlayer {

  private Branch[] branches;
  private int line;

 
  
  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {
    String[] splitByCommas = data.split(",");
    
    branches = new Branch[splitByCommas.length];
    
    for (int i = 0; i < splitByCommas.length; i++)
    {
      String branchName = splitByCommas[i].trim();
      branches[i] = new Branch(branchName);
      
    }
    
    this.line = line;
  }

  @Override
  public void execute(NPC npc, Player player) {
    for (Branch branch: branches)
    {
      ThreadHandler.branchInto(branch, npc, player, 
          ThreadHandler.currentThread());
    }
    
    ThreadHandler.lockCurrentThread(branches.length);
  }
  
  @Override
  public Request[] askForRequests()
  {
    Request[] requests = new Request[branches.length];
    
    for (int i = 0; i < requests.length; i++)
    {
      requests[i] = new CommandRequest(branches[i], line);
    }
    
    return requests;
  }
 
  
  
  
}
