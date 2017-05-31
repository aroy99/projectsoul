package komorebi.projsoul.script.decision;

import komorebi.projsoul.engine.ThreadHandler;
import komorebi.projsoul.entities.NPC;
import komorebi.projsoul.entities.player.Player;
import komorebi.projsoul.script.commands.abstracts.Command;

public class DecisionStatement {
  
  protected Command consequence;
  
  public void setConsequence(Command consequence)
  {
    this.consequence = consequence;
  }
  
  public void execute(NPC npc, Player player)
  {
    ThreadHandler.executeOnCurrentThread(consequence, npc, player);
  }
}
