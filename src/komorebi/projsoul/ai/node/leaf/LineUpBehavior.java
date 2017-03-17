/**
 * LineUpBehavior.java    Feb 8, 2017, 9:12:01 AM
 */
package komorebi.projsoul.ai.node.leaf;

import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.map.Map;

/**
 * The enemy lines up with the player
 *
 * @author Aaron Roy
 */
public class LineUpBehavior extends Behavior {
      
  private final float lineUpSpeed;
  private final int tolerance;
  
  public LineUpBehavior(Enemy parent, float lineUpSpeed, int tolerance) {
    super(parent);
    this.lineUpSpeed = lineUpSpeed;
    this.tolerance = tolerance;
  }

  @Override
  public Status update() {
    
    float targetX = Map.getPlayer().getX();
    float targetY = Map.getPlayer().getY();
          
    float x = parent.getX();
    float y = parent.getY();
          
    float deltaX = targetX-x;
    float deltaY = targetY-y;
      
    lineUp(deltaX, deltaY);
    
    if(closeEnoughtoAttack(targetX, targetY, x, y)){
      System.out.format("Enemy is close enough to attack with dir: %s\n", 
          parent.getDirection().toString());
      return Status.SUCCESS;
    }
    
    return Status.RUNNING;
  }

  private boolean closeEnoughtoAttack(float targetX, float targetY, float x, float y) {
    return Math.abs(targetX - x) < tolerance || Math.abs(targetY - y) < tolerance;
  }

  private void lineUp(float deltaX, float deltaY) {
    
    Face direction = Face.DOWN;
    float dx=0, dy=0;
        
    if(Math.abs(deltaX) < Math.abs(deltaY)){
      dx = Math.signum(deltaX)*lineUpSpeed;
  
      if (deltaY < 0) {
        direction = Face.DOWN;
      }
      else if (deltaY > 0) {
        direction = Face.UP;
      }
    }
  
    else if(Math.abs(deltaY) <= Math.abs(deltaX)){
      dy = Math.signum(deltaY)*lineUpSpeed;
  
      if (deltaX < 0) {
        direction = Face.LEFT;
      }
      else if (deltaX > 0) {
        direction = Face.RIGHT;
      }
    }
    
    parent.switchDirection(direction);
    parent.move(dx, dy);
  }
  
  @Override
  public BehaviorStates getState(){
    return BehaviorStates.LINE_UP;
  }
}
