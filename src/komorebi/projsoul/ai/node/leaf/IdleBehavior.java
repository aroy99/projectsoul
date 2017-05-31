/**
 * IdleBehavior.java    Feb 7, 2017, 9:14:56 AM
 */
package komorebi.projsoul.ai.node.leaf;

import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Enemy;

/**
 * The enemy does nothing
 *
 * @author Aaron Roy
 */
public class IdleBehavior extends Behavior{

  private int idleCount;
  private final int maxIdle;
  
  public IdleBehavior(Enemy parent, int maxIdle) {
    super(parent);
    this.maxIdle = maxIdle;
  }

  @Override
  public Status update() {
    idleCount--;
    parent.move(0, 0);
    
    if(idleCount <= 0){
      close();
      return Status.SUCCESS;
    }
    
    return Status.RUNNING;
  }
  
  public void close(){
    super.close();
    idleCount = GEN.nextInt(maxIdle)+maxIdle;
  }

}
