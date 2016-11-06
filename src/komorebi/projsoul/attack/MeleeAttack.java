package komorebi.projsoul.attack;

import java.awt.Rectangle;

import komorebi.projsoul.engine.Animation;
import komorebi.projsoul.entities.Characters;
import komorebi.projsoul.entities.Face;

public class MeleeAttack extends Attack {
  
  private Rectangle hitBox;

  private Animation leftAttack;
  private Animation rightAttack;
  private Animation upAttack;
  private Animation downAttack;

  private Face currentDir;
  private Characters character;

  /**
   * Creates an instance of a close-combat attack with rectangle-based
   * hit detection
   */
  public MeleeAttack(Characters c)
  {    
    
    this.character = c;
    
    switch (character)
    {
      case BRUNO:
        break;
      case CASPIAN:
        downAttack = new Animation(5, 4, 11, false);
        downAttack.add(24, 0, 16, 46, 0, 0);
        downAttack.add(49, 14, 19, 41, 0, -8);
        downAttack.add(74, 14, 19, 40, 0, -8);
        downAttack.add(100, 13, 26, 33, 0, 0);
        downAttack.add(127,6,35,40,-16,0);

        upAttack = new Animation(5,4,11,false);
        upAttack.add(26,55,16,47);
        upAttack.add(49,71,20,31,-4,0);
        upAttack.add(74,73,20,29,-4,0);
        upAttack.add(105,73,23,29,-7,0);
        upAttack.add(140,67,32,35);

        rightAttack = new Animation(5,4,11,false);
        rightAttack.add(27,110,16,47);
        rightAttack.add(51,127,29,30);
        rightAttack.add(88,127,29,30);
        rightAttack.add(126,125,28,32,-11,0);
        rightAttack.add(163,120,22,37);

        leftAttack = new Animation(5,4,11,false);
        leftAttack.add(27,110,16,47,0,true);
        leftAttack.add(51,127,29,30,-11,0,true);
        leftAttack.add(88,127,29,30,-11,0,true);
        leftAttack.add(126,125,28,32,0,true);
        leftAttack.add(163,120,22,37,0,true);
        break;
      case FLANNERY:
        break;
      case SIERRA:
        break;
      default:
        break;
      
    }

    

    hitBox = new Rectangle(0,0,16,16);

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

  /**
   * Indicates whether the current attack animation is currently playing
   * @return true if the attack animation is still playing, false if not
   */
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
  
  /**
   * Switches the current direction of the attack
   * @param dir The new direction of the attack
   */
  public void setDirection(Face dir)
  {
    currentDir = dir;
  }
  
  /**
   * Updates the location of the attack's hitbox based on the current direction
   * of the attack
   * @param x The player's x location
   * @param y The player's y location
   */
  public void update(int x, int y)
  {
    
    switch (currentDir)
    {
      case DOWN:
        hitBox.x = x;
        hitBox.y = y-16;
        break;
      case LEFT:
        hitBox.x = x-16;
        hitBox.y = y;
        break;
      case RIGHT:
        hitBox.x = x+16;
        hitBox.y = y;
        break;
      case UP:
        hitBox.x = x;
        hitBox.y = y+16;
        break;
      default:
        hitBox.x = x;
        hitBox.y = y;
        break;
      
    }
  }
  
  /**
   * @return The rectangle object representing the current hitbox of the attack
   */
  public Rectangle getHitBox()
  {
    return hitBox;
  }

}
