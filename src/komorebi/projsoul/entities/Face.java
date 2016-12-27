/**
 * Face.java  May 30, 2016, 12:06:22 PM
 */

package komorebi.projsoul.entities;

import komorebi.projsoul.engine.ThreadHandler.NewThread;

import java.util.Random;

/**
 * The four different directions someone can face, for animations
 * 
 * @author Aaron Roy
 * @version 0.0.2.0
 */
public enum Face{
    UP, DOWN, LEFT, RIGHT;
  
  private static final Random GEN = new Random();
  
  /**
   * @return The opposite direction of the object's current direction
   */
  public Face opposite()
  {
    switch (this)
    {
      case DOWN:
        return UP;
      case LEFT:
        return RIGHT;
      case RIGHT:
        return LEFT;
      case UP:
        return DOWN;
      default:
        return null;
    }
  }
  
  /**
   * @return A 90 degree rotation clockwise of the object's current direction
   */
  public Face rotate()
  {
    switch (this)
    {
      case DOWN:
        return LEFT;
      case LEFT:
        return UP;
      case RIGHT:
        return DOWN;
      case UP:
        return RIGHT;
      default:
        return null;
    }
  }
  
  /**
   * @return A random face
   */
  public static Face random(){
    int num = GEN.nextInt(4);
    
    switch (num){
      case 0: return UP;
      case 1: return DOWN;
      case 2: return LEFT;
      case 3: return RIGHT;
      default: return UP;
    }
  }

}
