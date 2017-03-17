/**
 * TiredBehavior.java    Feb 13, 2017, 9:46:08 AM
 */
package komorebi.projsoul.ai.node.leaf;

import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.map.Map;

/**
 * The enemy is tired and slowly follows the player
 *
 * @author Aaron Roy
 */
public class MoveToTarget extends Behavior{
  

  private static final int TOLERANCE = 12; //Prevents vibrating enemies
  
  private final float chaseSpeed;
  private final float closeEnough;
  
  public MoveToTarget(Enemy parent, float chaseSpeed, float closeEnough) {
    super(parent);
    this.chaseSpeed = chaseSpeed;
    this.closeEnough = closeEnough;
  }
  
  public MoveToTarget(Enemy parent, float chaseSpeed) {
    super(parent);
    this.chaseSpeed = chaseSpeed;
    this.closeEnough = 1;
  }

  @Override
  public Status update() {
    float targetX = parent.getTargetX();
    float targetY = parent.getTargetY();

    float x = parent.getX();
    float y = parent.getY();
    

    float tiX = Math.abs(targetX - x);
    float tiY = Math.abs(targetY - y);
    float theta = (float) Math.atan(tiY / tiX);

    float dx = 0, dy = 0;
    
    if(tiX < closeEnough && tiY < closeEnough){
      return Status.SUCCESS;
    }


    if (targetX > x && tiX > TOLERANCE) {
      dx = chaseSpeed * (float) Math.cos(theta);
    } else if (targetX < x) {
      dx = -chaseSpeed * (float) Math.cos(theta);
    }

    if (targetY > y && tiY > TOLERANCE) {
      dy = chaseSpeed * (float) Math.sin(theta);
    } else if (targetY < y) {
      dy = -chaseSpeed * (float) Math.sin(theta);
    }

    parent.move(dx, dy);

    return Status.RUNNING;
  }

  @Override
  public BehaviorStates getState() {
    return BehaviorStates.TIRED;
  }

}
