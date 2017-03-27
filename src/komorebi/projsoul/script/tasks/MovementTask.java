package komorebi.projsoul.script.tasks;

import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.Person.ActionState;
import komorebi.projsoul.script.Lock;

public class MovementTask extends TimedTask {

  private float dx, dy;
  
  public MovementTask(ActionState action, Precedence precedence, int frames,
      Face direction, Lock lock) {
    super(action, precedence, frames, lock);
    
    decrement = action == ActionState.JOGGING? 2: 1;
    determineSpeeds(direction, decrement);
    
 }
  
  private void determineSpeeds(Face dir, int velo)
  {
    switch (dir)
    {
      case LEFT:
        dx = -velo;
        break;
      case RIGHT:
        dx = velo;
        break;
      case UP:
        dy = velo;
        break;
      case DOWN:
        dy = -velo;
        break;
    }
  }
  
  public float getDx()
  {
    return dx;
  }
  
  public float getDy()
  {
    return dy;
  }

  
}
