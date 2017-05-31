/**
 * Arithmetic.java      May 22, 2017, 9:39:45 AM
 */
package komorebi.projsoul.engine;

import komorebi.projsoul.map.MapHandler;

/**
 * 
 *
 * @author Aaron Roy
 */
public class Arithmetic {

  /**
   * Calculates the distance between the enemy and the player
   * 
   * @param x
   *          The x of the enemy
   * @param y
   *          The y of the enemy
   * @param tarX
   *          The target X (i.e., the x of the player)
   * @param tarY
   *          The target Y (i.e., the y of the player)
   * @return The distance, as a double
   */
  public static float distanceBetween(float x, float y, float tarX,
      float tarY) {
    return (float) Math.sqrt(Math.pow((x - tarX), 2) + Math.pow((y - tarY), 2));
  }

  public static float angleOf(float x, float y, float tarX, float tarY) {
    float triX = x - tarX, triY = y - tarY;
    double ret = Math.atan(triY / triX) * (180 / Math.PI);
  
    if (triX < 0 && triY > 0) {
      ret += 180;
    } else if (triX < 0 && triY < 0) {
      ret -= 180;
    }
  
    return (float) ret;
  }

  public static int quadrantOf(float x, float y, float tarX, float tarY) {
    double angle = angleOf(x, y, tarX, tarY);
  
    if (angle > 0.5 && angle < 89.5) {
      return 1;
    } else if (angle > 90.5 && angle < 179.5) {
      return 2;
    } else if (angle > -179.5 && angle < -90.5) {
      return 3;
    } else if (angle > -89.5 && angle < -0.5) {
      return 4;
    } else if (Math.abs(angle) < MapHandler.TOLERANCE || 
        Math.abs(angle - 180) < MapHandler.TOLERANCE ||
        Math.abs(angle + 180) < MapHandler.TOLERANCE) {
      return 0;
    } else {
      return -1;
    }
  }

  public static float[] coordinatesAt(float cx, float cy, float dist,
      float ang) {
    float[] ret = new float[2];
  
    ret[0] = (float) (cx + Math.cos(ang * (Math.PI / 180)) * dist);
    ret[1] = (float) (cy + Math.sin(ang * (Math.PI / 180)) * dist);
  
    return ret;
  }

}
