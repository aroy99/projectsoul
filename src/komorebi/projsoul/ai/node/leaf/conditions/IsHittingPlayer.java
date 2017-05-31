/**
 * IsHittingPlayer.java    Mar 1, 2017, 9:33:37 AM
 */
package komorebi.projsoul.ai.node.leaf.conditions;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Enemy;

/**
 * Checks if the enemy just hit the player
 *
 * @author Aaron Roy
 */
public class IsHittingPlayer extends Node {

  protected Enemy parent;
  
  public IsHittingPlayer(Enemy parent) {
    this.parent = parent;
  }

  
  @Override
  public Status update() {
    if(parent.isHittingPlayer()){
      return Status.SUCCESS;
    }
    
    return Status.FAIL;
  }

}
