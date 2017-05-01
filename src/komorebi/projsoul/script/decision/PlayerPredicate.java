package komorebi.projsoul.script.decision;

import komorebi.projsoul.entities.player.Characters;
import komorebi.projsoul.map.Map;

public class PlayerPredicate implements Predicate {

  private Characters character;
  
  public PlayerPredicate(Characters character)
  {
    this.character = character;
  }
  
  public boolean evaluatesTrue() {
    return character == Map.getPlayer().getCharacter();
  }

  
}
