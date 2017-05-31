package komorebi.projsoul.script.commands;

import java.util.ArrayList;

import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.map.Map;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCAndPlayer;
import komorebi.projsoul.script.commands.keywords.Keyword;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.exceptions.UndefinedConstructorException;
import komorebi.projsoul.script.read.Branch;
import komorebi.projsoul.script.read.CommandRequest;
import komorebi.projsoul.script.read.Request;

public class AskCommand extends CommandOnNPCAndPlayer {

  private String[] branchNames;
  private Branch[] branches;
  
  private int line;

  private static final int MAX_ARGS = 5;
  
  @Override
  public void interpret(String data, int line) throws InvalidScriptSyntaxExceptionWithLine {

    this.line = line;
    
    if (data.isEmpty())
      throw new InvalidScriptSyntaxExceptionWithLine("The ask command requires arguments.",
          line);

    if (!data.startsWith("\""))
      throw new InvalidScriptSyntaxExceptionWithLine("The ask command requires "
          + "string arguments, surrounded by quotation marks.", line);

    int openQuote = 0;
    ArrayList<String> args = new ArrayList<String>();

    while (openQuote < data.length())
    {
      int closeQuote = data.indexOf("\"", openQuote + 1);
      if (closeQuote == -1)
        throw new InvalidScriptSyntaxExceptionWithLine("The string " + 
            data.substring(openQuote+1) + " is not terminated by a "
            + "quotation mark.", line);

      args.add(data.substring(openQuote + 1, closeQuote));

      openQuote = data.indexOf("\"", closeQuote + 1);
      if (openQuote == -1)
        openQuote = data.length();

      String whiteSpace = data.substring(closeQuote + 1, openQuote);

      if (!whiteSpace.trim().isEmpty())
        throw new InvalidScriptSyntaxExceptionWithLine("The ask commands requires"
            + " string arguments, surrounded by quotation marks. " + whiteSpace
            + " is not surrounded by quotation marks.", line);
    }
        
    if (args.size() > MAX_ARGS)
      throw new InvalidScriptSyntaxExceptionWithLine("The ask command can accept a "
          + "maximum of " + MAX_ARGS + " arguments: one for the question, "
              + "and " + (MAX_ARGS-1) + " for the responses.", line);
      
    
    branchNames = args.toArray(new String[args.size()]);
    branches = new Branch[branchNames.length-1];

    for (int i=1; i < branchNames.length; i++)
    {
      branches[i-1] = new Branch(branchNames[i]);
    }
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
  public void execute(NPC npc, Player player) {
    String[] sayThese = new String[branchNames.length];

    for (int i = 0; i < branchNames.length; i++)
    {
      if (i == 0)
      {
        sayThese[i] = branchNames[i].replace("@me", 
            Map.getPlayer().getCharacter().getNameFormatted());
      } else
      {
        sayThese[i] = branchNames[i];
      }
    }

    String answer = npc.ask(sayThese);

    Branch runThisBranch = getBranchWithName(answer);
    ThreadHandler.executeOnCurrentThread(runThisBranch, npc, player);
  }



}
