/**
 * SetTargetToPlayer.java    Mar 7, 2017, 9:36:02 AM
 */
package komorebi.projsoul.ai.node.leaf;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.map.MapHandler;

/**
 * @author Aaron Roy
 */
public class SetTargetToPlayer extends Node {

  private Enemy parent;

  public SetTargetToPlayer(Enemy parent) {
    this.parent = parent;
  }
  
  @Override
  public Status update() {
    System.out.println("Target set to playa");
    parent.setTarget(MapHandler.getPlayer().getX(), MapHandler.getPlayer().getY());
    
    return Status.SUCCESS;
  }

}
