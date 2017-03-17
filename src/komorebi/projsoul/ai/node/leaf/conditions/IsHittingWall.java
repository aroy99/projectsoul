/**
 * IsHittingWall.java    Mar 1, 2017, 9:32:29 AM
 */
package komorebi.projsoul.ai.node.leaf.conditions;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Enemy;

/**
 * 
 *
 * @author Aaron Roy
 */
public class IsHittingWall extends Node {

  protected Enemy parent;
  
  public IsHittingWall(Enemy parent) {
    this.parent = parent;
  }

  @Override
  public Status update() {
    if(parent.isHittingWall()){
      return Status.SUCCESS;
    }
    
    return Status.FAIL;
  }

}
