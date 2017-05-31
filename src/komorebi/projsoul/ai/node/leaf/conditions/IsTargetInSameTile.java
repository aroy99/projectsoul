/**
 * IsPlayerInSameTile.java     Mar 9, 2017, 9:12:56 AM
 */
package komorebi.projsoul.ai.node.leaf.conditions;

import komorebi.projsoul.ai.node.Node;
import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Enemy;

/**
 * Checks if the target is in the same tile
 *
 * @author Aaron Roy
 */
public class IsTargetInSameTile extends Node {

  Enemy parent;
  
  private int ptx, pty, ctx, cty; //Previous and current tile x
  
  public IsTargetInSameTile(Enemy parent) {
    this.parent = parent;
    
    ctx = (int)parent.getTargetX()/16;
    cty = (int)parent.getTargetY()/16;
  }

  @Override
  public Status update() {
    ptx = ctx;
    pty = cty;
    
    ctx = (int)parent.getTargetX()/16;
    cty = (int)parent.getTargetY()/16;

    if(ptx == ctx && pty == cty){
      return Status.SUCCESS;
    }
    return Status.FAIL;
  }

}
