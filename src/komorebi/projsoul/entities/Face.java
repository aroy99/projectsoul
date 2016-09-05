/**
 * Face.java  May 30, 2016, 12:06:22 PM
 */

package komorebi.projsoul.entities;

/**
 * The four different directions someone can face, for animations
 * 
 * @author Aaron Roy
 * @version 0.0.2.0
 */
public enum Face{
    UP, DOWN, LEFT, RIGHT;
  
  /**
   * Returns the opposite direction of the object's current direction
   * @return The opposite direction
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
}
