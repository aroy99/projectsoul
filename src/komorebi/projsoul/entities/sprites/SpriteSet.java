package komorebi.projsoul.entities.sprites;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.entities.Face;

/*
 * An object representing all of an NPC's sprites/animations
 */
public class SpriteSet {

  private Animation left, right, up, down;
  private Animation current;
  
  public SpriteSet(Animation left, Animation right, 
      Animation up, Animation down)
  {
    this.left = left;
    this.right = right;
    this.up = up;
    this.down = down;
    
    turn(Face.DOWN);
  }
  
  public SpriteSet duplicate()
  {    
    return new SpriteSet(left.duplicate(), right.duplicate(),
        up.duplicate(), down.duplicate());
  }
  
  public void turn(Face dir)
  {
    switch (dir)
    {
      case LEFT:
        current = left;
        break;
      case RIGHT:
        current = right;
        break;
      case UP:
        current = up;
        break;
      case DOWN:
        current = down;
        break;
    }
    
    current.resume();
  }
  
  public void renderCurrent(float x, float y)
  {
    current.playCam(x, y);
  }
  
  public void stopCurrent()
  {
    current.hStop();
  }
  
  public boolean isCurrentStopped()
  {
    return !current.playing();
  }
  
  public void resumeCurrent()
  {
    current.resume();
  }
  
  public void setAniSpeed(int speed)
  {
    current.setSpeed(speed);
  }
}
