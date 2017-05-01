package komorebi.projsoul.script.decision;

import java.util.ArrayList;

import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;

public class DecisionTree {
  
  private NPC npc;
  private Player player;
  
  private ArrayList<IfThenPair> ifStatements;
  private ElseThenPair elseStatement;
  
  public DecisionTree()
  {
    ifStatements = new ArrayList<IfThenPair>();
    
  }
  
  public void addIfStatement(IfThenPair add)
  {    
    ifStatements.add(add);
  }
  
  public boolean hasElse()
  {
    return elseStatement != null;
  }
  
  public void setElseStatement(ElseThenPair elseThen)
  {
    elseStatement = elseThen;
  }
  
  public void execute(NPC npc, Player player)
  {
    boolean anyEvaluatedTrue = false;
    int i = 0;
    
    while (!anyEvaluatedTrue && i < ifStatements.size())
    {
      if (ifStatements.get(i).evaluatesTrue())
      {
        ifStatements.get(i).execute(npc, player);
        anyEvaluatedTrue = true;
      }
      
      i++;
    }
    
    if (!anyEvaluatedTrue)
      elseStatement.execute(npc, player);
  }
}
