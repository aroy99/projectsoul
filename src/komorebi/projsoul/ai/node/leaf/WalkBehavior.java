/**
 * WalkBehavior.java    Feb 7, 2017, 9:24:35 AM
 */
package komorebi.projsoul.ai.node.leaf;

import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Enemy;

/**
 * The enemy walks for a bit
 *
 * @author Aaron Roy
 */
public class WalkBehavior extends Behavior{

  private int walkCount;
  private final int maxWalk;
  
  private final float walkSpeed;

  private static final BehaviorStates state = BehaviorStates.WALK;

  
  public WalkBehavior(Enemy parent, int maxWalk, float walkSpeed) {
    super(parent);
    this.maxWalk = maxWalk;
    this.walkSpeed = walkSpeed;
  }

  @Override
  public Status update() {
    walkCount--;
    
    if(walkCount <= 0){
      close();
      return Status.SUCCESS;
    }
    
    walk();
    
    return Status.RUNNING;
  }

  private void walk() {
    float dx = 0, dy = 0;
    
    switch (parent.getDirection()) {
      case DOWN: dy = -walkSpeed; break;
      case LEFT: dx = -walkSpeed; break;
      case RIGHT:dx =  walkSpeed; break;
      case UP:   dy =  walkSpeed; break;
      default:;
    }
    
    parent.move(dx, dy);
  }


  @Override
  public BehaviorStates getState(){
    return state;
  }
  
  @Override
  public void close(){
    super.close();
    walkCount = GEN.nextInt(maxWalk)+maxWalk;
    parent.randomizeDirection();
  }


}
