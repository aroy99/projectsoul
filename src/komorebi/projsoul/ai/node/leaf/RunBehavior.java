/**
 * RunBehavior.java     Feb 13, 2017, 9:12:24 AM
 */
package komorebi.projsoul.ai.node.leaf;

import komorebi.projsoul.ai.node.Status;
import komorebi.projsoul.entities.enemy.Enemy;
import komorebi.projsoul.map.MapHandler;

/**
 * The enemy runs away
 *
 * @author Aaron Roy
 */
public class RunBehavior extends Behavior {

  
  private static final int TOLERANCE = 12; //Prevents vibrating enemies
  
  final float runSpeed;
  
  public RunBehavior(Enemy parent, float runSpeed) {
    super(parent);
    this.runSpeed = runSpeed;
  }

  @Override
  public Status update() {
    float targetX = MapHandler.getPlayer().getX();
    float targetY = MapHandler.getPlayer().getY();

    float x = parent.getX();
    float y = parent.getY();

    float triX = Math.abs(targetX - x);
    float triY = Math.abs(targetY - y);
    float theta = (float) Math.atan(triY / triX);

    float dx = 0, dy = 0;

    if (targetX > x && triX > TOLERANCE) {
      dx = -runSpeed * (float) Math.cos(theta);
    } else if (targetX < x) {
      dx = runSpeed * (float) Math.cos(theta);
    }

    if (targetY > y && triY > TOLERANCE) {
      dy = -runSpeed * (float) Math.sin(theta);
    } else if (targetY < y) {
      dy = runSpeed * (float) Math.sin(theta);
    }

    parent.move(dx, dy);
    
    return Status.RUNNING;
  }

}
