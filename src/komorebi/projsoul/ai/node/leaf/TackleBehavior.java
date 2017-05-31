/**
 * TackleBehavior.java    Mar 1, 2017, 9:46:17 AM
 */
package komorebi.projsoul.ai.node.leaf;

import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.map.MapHandler;

/**
 * The enemy tries to tackle the player at a high speed
 *
 * @author Aaron Roy
 */
public class TackleBehavior extends Behavior {

  private final float tackleSpeed;
  
  public TackleBehavior(Enemy parent, float tackleSpeed) {
    super(parent);
    this.tackleSpeed = tackleSpeed;
  }

  @Override
  public Status update() {
    float targetX = MapHandler.getPlayer().getX();
    float targetY = MapHandler.getPlayer().getY();

    float x = parent.getX();
    float y = parent.getY();
    
    float dx = 0, dy = 0;
    
    float dist = 0;
    switch (parent.getDirection()) {
      case DOWN:  dist = targetY - y; break;
      case LEFT:  dist = targetX - x; break; 
      case RIGHT: dist = x - targetX; break; 
      case UP:    dist = y - targetY; break; 
      default:
    }
    
    if(dist > 48){
      return Status.FAIL;
    }
    switch (parent.getDirection())
    {
      case DOWN:
        dy = -tackleSpeed;
        break;
      case LEFT:
        dx = -tackleSpeed;
        break;
      case RIGHT:
        dx = tackleSpeed;
        break;
      case UP:
        dy = tackleSpeed;
        break;
      default:
        break;
    }
    parent.move(dx, dy);

    if(parent.isHittingSomething()){
      return Status.SUCCESS;
    }
    
    return Status.RUNNING;
  }
  
}
