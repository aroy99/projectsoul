/**
 * IsPlayerInRange.java    Feb 25, 2017, 8:36:25 AM
 */
package komorebi.projsoul.ai.node.leaf.conditions;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.map.Map;

/**
 * Checks whether the player is close enough to the enemy
 *
 * @author Aaron Roy
 */
public class IsPlayerInRange extends Node {

  private Enemy parent;
  
  private final int range;
  
  public IsPlayerInRange(Enemy parent, int range) {
    this.parent = parent;
    this.range = range;
  }

  @Override
  public Status update() {
    float currDist = calculateDistance();

    
    if(currDist < range){
      return Status.SUCCESS;
    }
    return Status.FAIL;
  }

  private float calculateDistance() {
    float targetX = Map.getPlayer().getX();
    float targetY = Map.getPlayer().getY();
    
    float x = parent.getX();
    float y = parent.getY();
    
    float currDist = Map.distanceBetween(x,y,targetX,targetY);
    return currDist;
  }

}
