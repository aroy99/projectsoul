/**
 * IsPlayerInRange.java    Feb 25, 2017, 8:36:25 AM
 */
package komorebi.projsoul.ai.node.leaf.conditions;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.engine.Arithmetic;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.map.MapHandler;

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
    float targetX = MapHandler.getPlayer().getX();
    float targetY = MapHandler.getPlayer().getY();
    
    float x = parent.getX();
    float y = parent.getY();
    
    float currDist = Arithmetic.distanceBetween(x,y,targetX,targetY);
    return currDist;
  }

}
