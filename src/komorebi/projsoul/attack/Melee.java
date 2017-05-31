package komorebi.projsoul.attack;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.entities.Face;
import komorebi.projsoul.entities.player.Characters;

public abstract class Melee implements AttackInstance {

  public static final float TOLERANCE = 0.0001f;
  
  public float x, y, dx, dy;
  public int attack;
  
  public Rectangle hitBox;

  public Animation leftAttack;
  public Animation rightAttack;
  public Animation upAttack;
  public Animation downAttack;

  public Face currentDir;
  public Characters character;

  public Melee(){}
  
  public Melee(float x, float y, Face dir, int attack)
  {
    this.x = x;
    this.y = y;
    this.currentDir = dir;
    this.attack = attack;
    
    this.hitBox = new Rectangle((int) x, (int) y,16,16);
  }
  
  public void play(float x, float y)
  {        
    switch (currentDir)
    {
      case DOWN:
        downAttack.playCam(x, y);
        break;
      case LEFT:
        leftAttack.playCam(x,y);
        break;
      case RIGHT:
        rightAttack.playCam(x, y);
        break;
      case UP:
        upAttack.playCam(x, y);
        break;
      default:
        break;
    }    
  }
  
  public boolean playing()
  {
    switch (currentDir)
    {
      case DOWN:
        return downAttack.playing();
      case LEFT:
        return leftAttack.playing();
      case RIGHT:
        return rightAttack.playing();
      case UP:
        return upAttack.playing();
      default:
        break;
    }

    return false;
  }

  /**
   * Starts a new attack, begins the animation
   * @param dir The direction the player is facing when he/she starts the attack
   */
  public void newAttack(Face dir)
  {
    currentDir = dir;

    switch (currentDir)
    {
      case DOWN:
        downAttack.resume();
        break;
      case LEFT:
        leftAttack.resume();
        break;
      case RIGHT:
        rightAttack.resume();
        break;
      case UP:
        upAttack.resume();
        break;
      default:
        break;
    }
  }
  
  public Rectangle getHitBox()
  {
    return hitBox;
  }
  
  public void update(int x, int y){}
  
  public void update()
  {
    
  }

  
  public void setDirection(Face dir)
  {
    this.currentDir = dir;
  }
  
  
  public float getX()
  {
    return x;
  }
  
  public float getY()
  {
    return y;
  }
  

  
  
}
