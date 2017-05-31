package komorebi.projsoul.script.commands;

import java.util.ArrayList;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.commands.abstracts.CommandOnNPCAndPlayer;
import komorebi.projsoul.script.decision.DecisionTree;
import komorebi.projsoul.script.decision.ElseStatement;
import komorebi.projsoul.script.decision.FlagPredicate;
import komorebi.projsoul.script.decision.Flags;
import komorebi.projsoul.script.decision.IfStatement;
import komorebi.projsoul.script.decision.PlayerPredicate;
import komorebi.projsoul.script.decision.Predicate;
import komorebi.projsoul.script.exceptions.InvalidScriptSyntaxExceptionWithLine;
import komorebi.projsoul.script.read.ConsequenceRequest;
import komorebi.projsoul.script.read.Request;

public class IfElseCommand extends CommandOnNPCAndPlayer {

  private ArrayList<ConsequenceRequest> requests;
  private DecisionTree decision;
  
  @Override
  public void execute(NPC npc, Player player) {
    decision.execute(npc, player);
    
  }

  @Override
  public void interpret(String data, int line) 
      throws InvalidScriptSyntaxExceptionWithLine {
    
    requests = new ArrayList<ConsequenceRequest>();
    decision = new DecisionTree();
    
    String[] lines = data.split("\n");
    
    for (int i = 1; i < lines.length - 1; i++)
    {
      if (!lines[i].startsWith("elseif"))
        throw new InvalidScriptSyntaxExceptionWithLine(
            "Decision structures must follow the format: "
            + "if-elseif-else", line);
    }
  
    createIfThenPair(lines[0], line);
    
    for (int i = 1; i < lines.length; i++)
    {
      lines[i] = lines[i].replaceFirst("else", "");
    }
    
    for (int i = 1; i < lines.length - 1; i++)
    {
      lines[i] = lines[i].replaceFirst("if", "");
      createIfThenPair(lines[i], line + i);
    }
    
    int last = lines.length - 1;
    
    if (lines[last].startsWith("if"))
    {
      lines[last] = lines[last].substring(3);
      createIfThenPair(lines[last], line + last);
    } else
    {
      createElseThenPair(lines[last], line + last);
    }
    
    
  }
  
  private void createIfThenPair(String data,
      int line) throws InvalidScriptSyntaxExceptionWithLine
  {
    data = data.trim();
    
    try
    {
      catchMissingComma(data, line, "if");
    } catch (Exception e)
    {
      throw e;
    }
    
    int comma = data.indexOf(",");
    String pred = data.substring(0, comma);
    String conseq = data.substring(comma + 1);
    
    conseq = conseq.trim();
    
    Predicate predicate = interpretPredicate(pred, line);
    IfStatement ifThen = new IfStatement(predicate);
    
    requests.add(new ConsequenceRequest(ifThen, conseq, line));
    decision.addIfStatement(ifThen);
    
  }
  
  private void createElseThenPair(String data, int line)
      throws InvalidScriptSyntaxExceptionWithLine
  {
    try
    {
      catchMissingComma(data, line, "else");
    } catch (Exception e)
    {
      throw e;
    }
    
    if (data.indexOf(",") != 0)
      throw new InvalidScriptSyntaxExceptionWithLine("The else keyword"
          + " must be followed by a comma", line);
    
    String conseq = data.substring(1);
    conseq = conseq.trim();
    
    ElseStatement elseThen = new ElseStatement();
    
    requests.add(new ConsequenceRequest(elseThen, conseq, line));
    decision.setElseStatement(elseThen);
  }
  
  public Request[] askForRequests()
  {
    return requests.toArray(new ConsequenceRequest[requests.size()]);
  }
  
  private void catchMissingComma(String data, int line, 
      String statementType) throws
      InvalidScriptSyntaxExceptionWithLine
  {
    if (!data.contains(","))
      throw new InvalidScriptSyntaxExceptionWithLine(
          "The predicate and consequence of a(n)" + statementType + 
          " statement must be separated by a comma", line);
  }
  
  private Predicate interpretPredicate(String predicate, int line)
    throws InvalidScriptSyntaxExceptionWithLine
  {
    for (Characters character: Characters.values())
    {
      if (character.toString().toLowerCase().equals(predicate))
        return new PlayerPredicate(character);
    }
    
    if (Flags.flagExists(predicate))
    {
      return new FlagPredicate(Flags.getFlagWithIdentifier(predicate));
    }
    
    throw new InvalidScriptSyntaxExceptionWithLine(predicate + 
        " does not evaluate either to a boolean flag or a player name", 
        line);
  }

  
}
