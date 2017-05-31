/**
 * Face.java  May 30, 2016, 12:06:22 PM
 */

package komorebi.projsoul.entities;

import java.util.Random;

/**
 * The four different directions someone can face, for animations
 * 
 * @author Aaron Roy
 * @version 0.0.2.0
 */
public enum Face{
    UP, DOWN, RIGHT, LEFT;
  
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
      case 2: return RIGHT;
      case 3: return LEFT;
      default: return UP;
    }
  }
  
  /**
   * @return The index of this face:
   *          UP: 0,
   *          DOWN: 1,
   *          LEFT: 2,
   *          RIGHT: 3,
   */
  public int getFaceNum(){
    switch(this){
      case UP:    return 0;
      case DOWN:  return 1;
      case RIGHT: return 2;
      case LEFT:  return 3;
      default:    return -1;
    }
  }

}
