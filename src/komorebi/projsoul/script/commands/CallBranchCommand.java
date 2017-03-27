package komorebi.projsoul.script.commands;

import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCAndPlayer;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxException;
import komorebi.projsoul.script.read.Branch;
import komorebi.projsoul.script.read.CommandRequest;
import komorebi.projsoul.script.read.Request;

public class CallBranchCommand extends CommandOnNPCAndPlayer {

  private Branch[] branches;

  @Override
  public void interpret(String data) throws InvalidScriptSyntaxException {
    String[] splitByCommas = data.split(",");
    
    branches = new Branch[splitByCommas.length];
    
    for (int i = 0; i < splitByCommas.length; i++)
    {
      String branchName = splitByCommas[i].trim();
      branches[i] = new Branch(branchName);
      
    }
  }

  @Override
  public void execute(NPC npc, Player player) {
    for (Branch branch: branches)
    {
      ThreadHandler.newThread(branch, npc, player);
    }
  }
  
  @Override
  public Request[] askForRequests()
  {
    Request[] requests = new Request[branches.length];
    
    for (int i = 0; i < requests.length; i++)
    {
      requests[i] = new CommandRequest(branches[i]);
    }
    
    return requests;
  }
  
  
  public static String keyword()
  {
    return "call";
  }
  
  
  
}
