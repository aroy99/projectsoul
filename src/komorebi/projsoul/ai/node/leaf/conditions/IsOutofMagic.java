/**
 * IsOutofMagic.java    Feb 24, 2017, 9:43:15 AM
 */
package komorebi.projsoul.ai.node.leaf.conditions;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.MagicEnemy;

/**
 * Checks if a magic user is out of magic
 *
 * @author Aaron Roy
 */
public class IsOutofMagic extends Node {

  protected MagicEnemy parent;
  private final int shootCost;
  
  public IsOutofMagic(MagicEnemy parent, int shootCost) {
    this.parent = parent;
    this.shootCost = shootCost;
  }

  @Override
  public Status update() {
    System.out.println(parent.getMagic());
    if(parent.getMagic() <= shootCost){
      return Status.SUCCESS;
    }
    return Status.FAIL;
  }

}
